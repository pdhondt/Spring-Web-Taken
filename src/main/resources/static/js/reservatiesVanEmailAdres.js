"use strict";
import {byId, toon, verberg, verwijderChildElementenVan} from "./util.js";

byId("toon").onclick = async function () {
    verbergReservatiesEnFouten();
    const emailInput = byId("email");
    if (! emailInput.checkValidity()) {
        toon("emailFout");
        emailInput.focus();
        return;
    }
    await findByEmail(emailInput.value);
}

function verbergReservatiesEnFouten() {
    verberg("reservatiesTable");
    verberg("emailFout");
    verberg("storing");
}

async function findByEmail(emailAdres) {
    const response = await fetch(`reservaties?emailAdres=${emailAdres}`);
    if (response.ok) {
        const reservaties = await response.json();
        toon("reservatiesTable");
        const reservatiesBody = byId("reservatiesBody");
        verwijderChildElementenVan(reservatiesBody);
        for (const reservatie of reservaties) {
            console.log(reservatie);
            const tr = reservatiesBody.insertRow();
            tr.insertCell().innerText = reservatie.id;
            tr.insertCell().innerText = reservatie.titel;
            tr.insertCell().innerText = reservatie.plaatsen;
            tr.insertCell().innerText = new Date(reservatie.besteld).toLocaleString("nl-BE");
        }
    } else {
        toon("storing");
    }
}

