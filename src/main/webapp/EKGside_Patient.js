let i;
let sessionid;
let boxnr;

function cprSearch() {
    let cpr = document.getElementById("cpr").value;
    if (cpr.length === 10) {
    }

    fetch("data/ekgSessions/ekgSessionJson?" + new URLSearchParams({
        cpr: cpr
    }), {
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(resp => resp.json()).then(data => makeCheckbox(data));
}


function makeCheckbox(data) {
    let timestart = "";
    let container = "";
    let container2 = "";
    let sesid = "";
    let marker = "";
    let note = "";
    let klinikID = "";

    for (i = 0; i < data.ekgSessionList.length; i++) {
        timestart = data.ekgSessionList[i].timeStart;
        sesid = data.ekgSessionList[i].sessionID;
        marker = data.ekgSessionList[i].markers;
        note = data.ekgSessionList[i].comment;
        klinikID = data.ekgSessionList[i].klinikID;

        let Checkbox = '<span class="Check_Box">' + timestart + '<input type="checkbox" onclick="showMeasurement(' + sesid + ',' + i + ',' + klinikID + ')" id="checkbox' + i + '">' + '</span>' + '<hr>';
        let Comment = '<span class="commentbox" id="comment' + i + '" hidden>KlinikID: ' + klinikID + '  <br>    ' + 'Sessionid: ' + sesid + '<br> Marker:<br><p id="marker' + i + '">' + marker + '</p><br> Note:<br><textarea style="width: 225px" id="textarea' + i + '" disabled>' + note + '</textarea><hr></span>'
        container2 += Comment;


        container += Checkbox;
    }
    document.getElementById("autoID").innerHTML = container;
    document.getElementById("autoComment").innerHTML = container2;
}

function showMeasurement(sesID, boxNR, klinid) {
    let boxnr = boxNR;
    sessionid = sesID;
    let comment;
    for (let l = 0; l < i; l++) {
        comment = "comment" + l;
        if (l == boxnr) {
            document.getElementById(comment).removeAttribute("hidden");
        } else {
            let checkbox = "checkbox" + l;
            document.getElementById(checkbox).checked = false;
            document.getElementById(comment).setAttribute("hidden", "");
        }
    }
    ekgMeasFetch(sesID, klinid)
}


function ekgMeasFetch(sesID, klinid) {
    fetch("data/ekgSessions/ekgMeasurementsJson?" + new URLSearchParams({
        sessionID: sesID,
        klinikID: klinid
    }), {
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(resp => resp.json()).then(data => makeChart(data.measurment));
}

function makeChart(array) {

    let label = [0];
    for (let i = 1; i < array.length; i++) {
        label.push(i);
    }

    myChart.data.labels = label;
    myChart.data.datasets[0].data = array; // Would update the first dataset's value of 'March' to be 50
    myChart.update(); //

}

let data = {
    labels: [],
    datasets:
        [
            {
                label: "ekg",
                backgroundColor: 'rgb(0, 0, 0)',
                borderColor: 'rgb(255, 0, 0)',
                color: 'rgb(255,0,0)',

                data: [],
                tension: 1,
                pointRadius: 0.5,
                pointHoverRadius: 0.5
            }
        ]
};


let config = {
    type: 'line',
    data: data,
    option: {}
}

let myChart = new Chart(
    document.getElementById('myChart'),
    config
);

function updateEkgSession() {
    let endpoint = 'data/ekgSessions/ekgSessionJson/' + sessionid + '/comment';
    fetch(endpoint
        , {
            method: "PUT",
            body: document.getElementById(("textarea" + boxnr)).value,
            headers: {
                "Authorization": localStorage.getItem("token")
            }
        }).then(res => res.statusText).then(res => alert(res));
}

window.onload = function () {
    cprSearch()
    document.getElementById("cpr").value = sessionStorage.getItem("user");
}
