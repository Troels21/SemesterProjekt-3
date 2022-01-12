let tok = localStorage.getItem("token");
if (!tok) {
    window.location.href = "LoginSide.html"
}
let counter;

function hentAftaleFecth() {
    fetch("data/aftaler/aftalerSQL?", {
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
        klinikId = ("klinikId: " + data.aftaleListe[counter].klinikID);
        cpr = "CPR: " + data.aftaleListe[counter].CPR + "\t";
        note = "Notat: " + data.aftaleListe[counter].notat;
        aftaleID = data.aftaleListe[counter].ID;


        let Tider = '<span class="autotider">' + timestart + timeend + '</span>';
        let CPR = '<span class="autoname">' + cpr + klinikId + '</span>';
        let Notat = '<span class="autonote">' + note + '</span><hr>';

        container += Tider + CPR + Notat;
    }

    document.getElementById("autotider").innerHTML = container;
}

function deleteAftale() {
    let numberToDelete = "(0"
    for (let l = 0; l < counter; l++) {
        let box = "checkbox" + l;

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
        }).then(resp => alert(resp.text)).then(refresh);
}

//Pop-up journal
function formfetch() {
    fetch("data/aftaler/aftalerSQL?" + new URLSearchParams({
        cpr: document.getElementById("cpr").value,
        //name: document.getElementById("navn").value,
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
            alert("OK");
        } else {
            throw Error(await resp.text());
        }
    }).then(refresh).catch(Error => alert(Error))
}

function openForm() {
    document.getElementById("myForm").style.display = "block";
    document.getElementById("cpr").value = sessionStorage.getItem("user");

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
        alert('Weekends not allowed');
        datetime.value = "";
        start.value = "";
        end.value = "";
        timefree.value = "";
    }
    if (day.getHours() > 16 || day.getHours() < 8) {
        alert('Between 8-16');
        datetime.value = "";
        start.value = "";
        end.value = "";
        timefree.value = "";
    }
}

function refresh() {
    hentAftaleFecth()
}

function logud() {
    sessionStorage.setItem("user", "");
    window.location.replace("LoginSide.html");
}

function showTime() {
    var date = new Date();
    let dato = `${date.getUTCDate()}/${date.getUTCMonth()+1}/${date.getUTCFullYear()}`
    var time = date.getHours();
    var minut = date.getMinutes();

    if (minut < 10) {
        minut = "0" + minut;
    }

    document.getElementById("MyClockDisplay").innerText = `${dato} kl. ${time}:${minut}` //kl. " + time + ":" + minut; // +":"+sekunder;

    setTimeout(showTime, 10000,); //Tiden kan ændres, hvis vi er begrænset på processernes kapicitet
}

window.onload = function () {
    refresh();
    document.getElementById("brugernavn").innerText = "CPR: "+sessionStorage.getItem("user");
    showTime();
}
