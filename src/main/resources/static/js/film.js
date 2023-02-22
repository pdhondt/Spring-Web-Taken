"use strict";
import {byId, toon, verberg, setText} from "./util.js";

byId("zoek").onclick = async function () {
    verbergFilmEnFouten();
    const zoekIdInput = byId("zoekId");
    if (zoekIdInput.checkValidity()) {
        findById(zoekIdInput.value);
    } else {
        toon("zoekIdFout");
        zoekIdInput.focus();
    }
}

function verbergFilmEnFouten() {
    verberg("film");
    verberg("zoekIdFout");
    verberg("nietGevonden");
    verberg("storing");
    verberg("nieuweTitelFout");
}

async function findById(id) {
    const response = await fetch(`films/${id}`);
    if (response.ok) {
        const film = await response.json();
        toon("film");
        setText("titel", film.titel);
        setText("jaar", film.jaar);
        setText("vrijePlaatsen", film.vrijePlaatsen);
    } else {
        if (response.status === 404) {
            toon("nietGevonden");
        } else {
            toon("storing");
        }
    }
}

byId("verwijder").onclick = async function () {
    const zoekIdInput = byId("zoekId");
    const response = await fetch(`films/${zoekIdInput.value}`, {method : "DELETE"});
    if (response.ok) {
        verbergFilmEnFouten();
        zoekIdInput.value = "";
        zoekIdInput.focus();
    } else {
        toon("storing");
    }
}

byId("wijzigTitel").onclick = async function () {
    verberg("nieuweTitelFout");
    const nieuweTitelInput = byId("nieuweTitel");
    if (! nieuweTitelInput.checkValidity()) {
        toon("nieuweTitelFout");
        nieuweTitelInput.focus();
        return;
    }
    const nieuweTitel = {
        titel: nieuweTitelInput.value
    }
    updateTitel(nieuweTitel);
}

async function updateTitel(gewijzigdeTitel) {
    const response = await fetch(`films/${byId("zoekId").value}/titel`,
        {
            method: "PATCH",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(gewijzigdeTitel)
        });
    if (response.ok) {
        setText("titel", gewijzigdeTitel.titel);
    } else if (response.status === 400) {
        toon("nieuweTitelFout");
    } else {
        toon("storing");
    }
}

byId("reserveer").onclick = async function () {
    verberg("emailAdresFout");
    verberg("aantalTicketsFout");
    const emailAdresInput = byId("emailAdres");
    if (! emailAdresInput.checkValidity()) {
        toon("emailAdresFout");
        emailAdresInput.focus();
        return;
    }
    const aantalTicketsInput = byId("aantalTickets");
    if (! aantalTicketsInput.checkValidity()) {
        toon("aantalTicketsFout");
        aantalTicketsInput.focus();
        return;
    }
    const nieuweReservatie = {
        "emailAdres": emailAdresInput.value,
        "aantalTickets": aantalTicketsInput.value
    }
    reserveer(nieuweReservatie);
}

async function reserveer(reservatie) {
    verberg("nietGevonden");
    verberg("conflict");
    verberg("storing");
    const response = await fetch(`films/${byId("zoekId").value}/reservatie`,
        {
            method: "POST",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(reservatie)
        })
    if (response.ok) {
        window.location = "allefilms.html";
    } else {
        switch (response.status) {
            case 404:
                toon("nietGevonden");
                break;
            case 409:
                const responseBody = await response.json();
                setText("conflict", responseBody.message);
                toon("conflict");
                break;
            default:
                toon("storing");
        }
    }
}
