let tok = localStorage.getItem("token");
if (!tok) {
    window.location.href = "index.html"
}
let counter;

function hentAftaleFecth(from, to) {
    let fra = from;
    let til = to;

    fetch("data/aftaler/aftalerSQL?" + new URLSearchParams({
        from: fra,
        to: til,

    }), {
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(resp => resp.json()).then(data => udfyldskema(data));
}

function udfyldskema(data) {
    let timestart = "";
    let timeend = "";
    let klinikId = "";
    let cpr = "";
    let container = "";
    let note = "";

    for (counter = 0; counter < data.aftaleListe.length; counter++) {
        timestart = data.aftaleListe[counter].timeStart.substring(11, 16) + "\t-\t";
        timeend = data.aftaleListe[counter].timeEnd.substring(11, 16)
        klinikId = data.aftaleListe[counter].klinikID;
        cpr = "CPR: " + data.aftaleListe[counter].CPR + "\t";
        note = "Notat: " + data.aftaleListe[counter].notat;
        aftaleID = data.aftaleListe[counter].ID;



        if (klinikId == 3) {
            let Tider = '<span class="autotider">' + timestart + timeend + '</span>';
            let CPR = '<span class="autoname">' + cpr + 'klinikId: ' +klinikId + '</span>';
            let Notat = '<span class="autonote">' + note + '</span>';
            let Checkbox = '<span class="R_Button">' + '<input type="checkbox" id="checkbox' + counter + '" name="' + aftaleID + '">' + '</span><hr>';
            container += Tider + CPR + Notat + Checkbox;
        } else {
            let Tider = '<span class="autotider">' + timestart + timeend + '</span>';
            let CPR = '<span class="autoname">' + cpr + 'klinikId: ' +klinikId + '</span>';
            let Notat = '<span class="autonote">' + note + '</span><hr>';
            container += Tider + CPR + Notat;
        }
    }

    document.getElementById("autotider").innerHTML = container;
}

function deleteAftale() {
    let numberToDelete = "(0"
    for (let l = 0; l < counter; l++) {
        let box = "checkbox" + l;
        console.log(box);
        console.log()

        if (document.getElementById(box).checked) {
            numberToDelete += "," + document.getElementById(box).getAttribute("name");
        }
    }
    numberToDelete += ")";
    fetch("data/aftaler/aftalerSQL?" + new URLSearchParams({
        numberToDelete: numberToDelete
    })
        , {
            method: "DELETE",
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        }).then(resp => alert("De valgte aftaler er hermed slettet fra systemet.")).then(refresh);
}

//Kalendar
const months = [
    "Januar",
    "Febuar",
    "Marts",
    "April",
    "Maj",
    "Juni",
    "Juli",
    "August",
    "September",
    "Oktober",
    "November",
    "December",
];
let fromfrom = "";
let tiltil = "";


function makecalender(date) {
    let mymonth = months[date.getMonth()];


    const dates = document.querySelector(".dates");
    const lastdates = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();

    document.getElementById("actualmonth").innerText = mymonth + "    " + date.getFullYear();

    const firstdayindex = date.getDay() - 1;

    const prevlastdates = new Date(date.getFullYear(), date.getMonth(), 0).getDate()
    const nextdayindex = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDay();
    const nextdays = 7 - nextdayindex;

    let year = date.getFullYear();
    let month = date.getMonth() + 1;

    let days = "";

    for (let x = firstdayindex; x > 0; x--) {
        days += `<div class="lastdates"  onclick="prevdate();setdates(${year},${month - 1 === 0 ? 12 : month - 1},${prevlastdates - x + 1})">${prevlastdates - x + 1}</div>`;
    }

    for (let z = 1; z <= lastdates; z++) {

        days += `<div id="${year},${month},${z}" onclick="setdates(${year},${month},${z})">${z}</div>`;

    }
    for (let y = 1; y <= nextdays; y++) {
        days += `<div class="nextdates" onclick="nextdate();setdates(${year},${month + 1 === 13 ? 1 : month + 1},${y})">${y}</div>`;

    }
    dates.innerHTML = days;
}

let date = new Date();
makecalender(date);

function nextdate() {
    date.setMonth(date.getMonth() + 1);
    makecalender(date);
}

function prevdate() {
    date.setMonth(date.getMonth() - 1);
    makecalender(date);

}

let i = 0;

function setdates(year, month, day) {

    fromfrom = (year + "-" + month + "-" + day);
    tiltil = (year + "-" + month + "-" + (day + 1));
    document.getElementById("autotiderbar").innerText = "Den  " + day + "/" + month;

    //Hvis der allerede er en aktiv klasse. Dvs en dato allerede er trykket på.
    const active = document.getElementsByClassName("active")
    if (active.length > 0) {
        active[0].className = ""; //Fjerner den aktive klasse.
    }
    //Gør klassen (den enkelte dato der bliver trykket på aktiv.
    document.getElementById(`${year},${month},${day}`).className = "active";
    if (i === 0) {
        hentAftaleFecth(fromfrom, tiltil);
        setInterval(function () {
        }, 10000);
        i++;
    } else {
        hentAftaleFecth(fromfrom, tiltil);
    }
}

//Pop-up journal
function formfetch() {
    fetch("data/aftaler/aftalerSQL?" + new URLSearchParams({
        cpr: document.getElementById("cpr").value,
        timestart: document.getElementById("timeStart").value,
        timeend: document.getElementById("timeEnd").value,
        note: document.getElementById("textarea").value
    }), {
        method: "POST",
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(async resp => {
        if (resp.status >= 200 && resp.status <= 299) {
            alert("Aftalen er hermed oprettet i systemet.");
        } else {
            throw Error(await resp.text());
        }
    }).then(refresh).catch(Error => alert("Aftalen kunne ikke oprettes i systemet."));


}

function openForm() {
    document.getElementById("myForm").style.display = "block";
}

function closeForm() {
    document.getElementById("myForm").style.display = "none";
    resetForm()
}

function submitForm() {
    document.getElementById("myForm").style.display = "none";
    formfetch()
    resetForm()
}

function resetForm() {
    document.getElementById("cpr").value = "";
    //document.getElementById("navn").value = "";
    document.getElementById("timeStart").value = "";
    document.getElementById("timeEnd").value = "";
    document.getElementById("timefree").value = "";
    document.getElementById("textarea").value = "";
}

function noWeekend() {
    let datetime = document.getElementById('datetime');

    let day = new Date(datetime.value);
    let endDay = new Date(datetime.value);

    let time = day.getMinutes();

    if (time <= 8) {
        day.setMinutes(0);

        endDay.setMinutes(15);
    }
    if (time > 8 && time <= 23) {
        day.setMinutes(15);

        endDay.setMinutes(30);
    }
    if (time > 23 && time <= 38) {
        day.setMinutes(30);

        endDay.setMinutes(45);
    }
    if (time > 38 && time <= 53) {
        day.setMinutes(45);

        endDay.setMinutes(0);
        endDay.setHours(endDay.getHours() + 1);
    }
    if (time >= 53) {
        day.setMinutes(0);
        day.setHours(day.getHours() + 1);

        endDay.setMinutes(15);
        endDay.setHours(endDay.getHours() + 1);
    }

    let start = document.getElementById('timeStart');
    let end = document.getElementById('timeEnd')
    let timefree = document.getElementById("timefree")
    start.value = (day.getFullYear() + "-" + (day.getMonth() + 1) + "-" + day.getUTCDate() + " " + day.getHours() + ":" + day.getMinutes());
    end.value = (endDay.getFullYear() + "-" + (endDay.getMonth() + 1) + "-" + endDay.getUTCDate() + " " + endDay.getHours() + ":" + endDay.getMinutes());
    timefree.value = (day.getHours() + ":" + day.getMinutes() + " til " + endDay.getHours() + ":" + endDay.getMinutes() + "    d." + day.getFullYear() + "-" + (day.getMonth() + 1) + "-" + day.getUTCDate())

    if (day.getDay() === 6 || day.getDay() === 0) {
        alert('Tider kan kun bestilles fra mandag til fredag.');
        datetime.value = "";
        start.value = "";
        end.value = "";
        timefree.value = "";
    }
    if (day.getHours() > 16 || day.getHours() < 8) {
        alert('Tider kan kun bestilles inden for 8-16.');
        datetime.value = "";
        start.value = "";
        end.value = "";
        timefree.value = "";
    }
}

function showTime() {
    var date = new Date();
    let dato = `${date.getUTCDate()}/${date.getUTCMonth() + 1}/${date.getUTCFullYear()}`
    var time = date.getHours();
    var minut = date.getMinutes();

    if (minut < 10) {
        minut = "0" + minut;
    }

    document.getElementById("MyClockDisplay").innerText = `${dato} kl. ${time}:${minut}` //kl. " + time + ":" + minut; // +":"+sekunder;

    setTimeout(showTime, 10000,); //Tiden kan ændres, hvis vi er begrænset på processernes kapicitet
}

function refresh() {
    hentAftaleFecth(fromfrom, tiltil)
}

document.getElementById("brugernavn").innerText = "User: " + sessionStorage.getItem("user");

function logud() {
    sessionStorage.setItem("user", "");
    window.location.replace("index.html");
}

window.onload = function () {
    showTime()
}