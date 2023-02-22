"use strict";
import {byId, toon, verberg} from "./util.js";

byId("toevoegen").onclick = async function () {
    verbergFouten();
    const titelInput = byId("titel");
    if (! titelInput.checkValidity()) {
        toon("titelFout");
        titelInput.focus();
        return;
    }
    const jaarInput = byId("jaar");
    if (! jaarInput.checkValidity()) {
        toon("jaarFout");
        jaarInput.focus();
        return;
    }
    const film = {
        titel: titelInput.value,
        jaar: jaarInput.value
    }
    await voegToe(film);
}

function verbergFouten() {
    verberg("titelFout");
    verberg("jaarFout");
    verberg("storing");
}

async function voegToe(film) {
    const response = await fetch("films", {
        method: "POST",
        headers: {'Content-Type': "application/json"},
        body: JSON.stringify(film)
    });
    if (response.ok) {
        window.location = "allefilms.html";
    } else {
        toon("storing");
    }
}