let i;
let myChart = null;


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

    for (i = 0; i < data.ekgSessionList.length; i++) {
        timestart = data.ekgSessionList[i].timeStart;
        sesid = data.ekgSessionList[i].sessionID;
        marker = data.ekgSessionList[i].markers;
        note = data.ekgSessionList[i].comment;

        let Checkbox = '<span class="Check_Box">' + timestart + '<input type="checkbox" onclick="showMeasurement(' + sesid + ',' + i + ')" id="checkbox' + i + '">' + '</span>' + '<hr>';
        let Comment = '<span class="commentbox" id="comment' + i + '" hidden>marker:<br>' + marker + '<br> note:<br><textarea style="width: 225px">' + note + '</textarea><hr></span>'


        container += Checkbox;
        container2 += Comment;
    }
    document.getElementById("autoID").innerHTML = container;
    document.getElementById("autoComment").innerHTML = container2;
}

function showMeasurement(sesID, boxNR) {
    for (let l = 0; l <= i; l++) {
        let comment = "comment" + l;
        document.getElementById(comment).hidden = true;
        if (!l == boxNR) {
            let checkbox = "checkbox" + l;
            document.getElementById(checkbox).checked = false;
            document.getElementById(comment).hidden = false;
            if (!myChart === null) {
                myChart.clear();
            }
        }
        ekgMeasFetch(sesID)
    }
}


function ekgMeasFetch(sesID) {
    fetch("data/ekgSessions/ekgMeasurementsJson?" + new URLSearchParams({
        sessionID: sesID
    }), {
        headers: {
            "Authorization": localStorage.getItem("token")
        }
    }).then(resp => resp.json()).then(data => makeChart(data.measurments));
}

function makeChart(array) {

    let labels = [0];
    for (let i = 1; i < 2000; i++) {
        labels.push(i);
    }


    let data = {
        labels: labels,
        datasets:
            [
                {
                    label: "ekg",
                    backgroundColor: 'rgb(0, 0, 0)',
                    borderColor: 'rgb(255, 0, 0)',
                    color: 'rgb(0,0,0)',

                    data: array,
                    tension: 1,
                    pointRadius: 1.5,
                    pointHoverRadius: 1
                }
            ]
    };


    let config = {
        type: 'line',
        data: data,
        option: {}
    }


    mychart = new Chart(
        document.getElementById('myChart'),
        config
    );


}
