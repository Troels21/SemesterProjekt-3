function LineChart(con) {
    // user defined properties
    this.canvas = document.getElementById(con.canvasId);
    this.minX = con.minX;
    this.minY = con.minY;
    this.maxX = con.maxX;
    this.maxY = con.maxY;
    this.unitsPerTickX = con.unitsPerTickX;
    this.unitsPerTickY = con.unitsPerTickY;

    // constants
    this.padding = 10;
    this.tickSize = 10;
    this.axisColor = "#555";
    this.pointRadius = 5;
    this.font = "12pt Calibri";

    this.fontHeight = 12;

    // relationships
    this.context = this.canvas.getContext("2d");
    this.rangeX = this.maxX - this.minY;
    this.rangeY = this.maxY - this.minY;
    this.numXTicks = Math.round(this.rangeX / this.unitsPerTickX);
    this.numYTicks = Math.round(this.rangeY / this.unitsPerTickY);
    this.x = this.getLongestValueWidth() + this.padding * 2;
    this.y = this.padding * 2;
    this.width = this.canvas.width - this.x - this.padding * 2;
    this.height = this.canvas.height - this.y - this.padding - this.fontHeight;
    this.scaleX = this.width / this.rangeX;
    this.scaleY = this.height / this.rangeY;

    // draw x y axis and tick marks
    this.drawXAxis();
    this.drawYAxis();
}


const myChart = new Chart(
    document.getElementById('myChart'),
    config
);


/*window.onload=(function (){

    let canvas = document.getElementById("myChart")
    let context = canvas.getContext("2d")
    context.moveTo(0,300)
    for (let i = 0; i<data.length; i++){
        context.lineTo(i,300-data[i]*100);
        context.stroke();
    }
    context.restore();
};
LineChart.prototype.drawYAxis = function () {
    var context = this.context;
    context.save();
    context.save();
    context.beginPath();
    context.moveTo(this.x, this.y);
    context.lineTo(this.x, this.y + this.height);
    context.strokeStyle = this.axisColor;
    context.lineWidth = 2;
    context.stroke();
    context.restore();

    // draw tick marks
    for (var n = 0; n < this.numYTicks; n++) {
        context.beginPath();
        context.moveTo(this.x, n * this.height / this.numYTicks + this.y);
        context.lineTo(this.x + this.tickSize, n * this.height / this.numYTicks + this.y);
        context.stroke();
    }

// draw values
    context.font = this.font;
    context.fillStyle = "black";
    context.textAlign = "right";
    context.textBaseline = "middle";

    for (var n = 0; n < this.numYTicks; n++) {
        var value = Math.round(this.maxY - n * this.maxY / this.numYTicks);
        context.save();
        context.translate(this.x - this.padding, n * this.height / this.numYTicks + this.y);
        context.fillText(value, 0, 0);
        context.restore();
    }
    context.restore();
};


LineChart.prototype.drawLine = function (data, color, width) {
    var context = this.context;
    context.save();
    this.transformContext();
    context.lineWidth = width;
    context.strokeStyle = color;
    context.fillStyle = color;
    context.beginPath();
    context.moveTo(data[0].x * this.scaleX, data[0].y * this.scaleY);

    for (var n = 0; n < data.length; n++) {
        var point = data[n];

        // draw segment
        context.lineTo(point.x * this.scaleX, point.y * this.scaleY);
        context.stroke();
        context.closePath();
        context.beginPath();
        context.arc(point.x * this.scaleX, point.y * this.scaleY, this.pointRadius, 0, 2 * Math.PI, false);
        context.fill();
        context.closePath();

        // position for next segment
        context.beginPath();
        context.moveTo(point.x * this.scaleX, point.y * this.scaleY);
    }
    context.restore();
};

window.onload = function () {
    var myLineChart = new LineChart({
        canvasId: "myCanvas",
        minX: 0,
        minY: 0,
        maxX: 140,
        maxY: 100,
        unitsPerTickX: 10,
        unitsPerTickY: 10
    });
}