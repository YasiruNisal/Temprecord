var value;//total set of details
var hour = "hh"; // hh for 12hr && HH for 24hr
var timesuffix = "TT"; // TT for AM/PM &&
var axisY1suffix = ''; //channel one unit
var axisY2suffix = ''; //channel two unit 
var Y1suffix;
var chart;
var tag = 100;
var clickLine;
var plotband1;
var plotband2;
var startdate = 0;
var stopdate = 0;
var zoomxInterval = 0;
var zoomyInterval = 0;
var zoomy2Interval = 0;
var minY = 100;
var minY2 =  100;
var maxY = -100;
var maxY2 = -100;
var TUL = 0;
var TLL = 0;
var HUL = 0;
var HLL = 0;
var yVisible = false;
var y2Visible = false;
var visible = 0;
var clickDetected = false;
var clicknumber = 0;
var timestamp = 0;
var selectedstartdate = 0;
var selectedstopdate = 0;
var Xinfo = 0;
var indexcolor = 0;
var removeX = 0;
var fromJava =0;
var plotOptions = 0;
var jsonfile = 0;
var navigatorenable = true;
var deviceinfo;
var initialY2= 0;
var initialY2Limit = 0;
var sendJson = 0; 
var eventflag = false;

var graphBackground
var graphBorder
var grid
var font
var fontcolor
var Upperline1
var line1
var Lowerline1
var Upperline2
var line2
var Lowerline2
var linewidth
var gridwidth
var graphColor
var UpperLimitLine
var UpperLimitArea
var BelowLimitLine
var BelowLimitArea
var WithinLimitArea

//intialize the graph
window.onload = function()
{

    setTheme(1);
    /*
    deviceinfo is the json file from Tempwrite
    Tempwrite is a desktop application
    */
    if(deviceinfo != undefined)
    {
        jsonfile = deviceinfo;
        setSettings(jsonfile);
    }

    /*
    if the jsonfile = 0, no json file was found from a desktop application
    android.getData() gets jsonfile from mobile app.
    */
    if(jsonfile == 0)
    {
        fromJava = android.getData();
        jsonfile = JSON.parse(fromJava);
        setAppSetting(jsonfile);
    }

    /*
    Initializes the graph itself. 
    Graph Library: HIGHCHARTS
    function details can be found on website above.
    */
    value = getData(jsonfile);
    chart = Highcharts.stockChart('container', {

        //pre-set ranges for zoom 
        //disabled to allowed custom zoom. 
        rangeSelector: {
            enabled: false
        },

        //preview bar at the bottom of the graph
        navigator: {
            height: 40,
            enabled: navigatorenable
        },

        //graph itself
        chart: {
            animation: false,
            type: 'line',
            plotBorderWidth: 1,
            plotBorderColor: graphBorder,
            zoomType: 'xy',
            panKey: 'shift',
             backgroundColor: graphBackground,
             style: {
                textTransform: 'uppercase',
                fontFamily: 'oswald',
                fontSize: '12px'
            },
            resetZoomButton: {
                theme: {
                    display: 'none'
                }
            }
        },

        tooltip: {
            split: true,
            crosshairs: true,
            valueDecimals: 2
        },

        legend: {
            enabled: true,
            //floating: true,
            align: 'center',
            backgroundColor: 'none',
            layout: 'horizontal',
            verticalAlign: 'top',
            borderWidth: 0
        },


        xAxis: [{
            type: 'datetime',
            dateTimeLabelFormats: {
                second: '%m/%d/%Y<br> %H:%M:%S',
                minute: '%m/%d/%Y<br> %H:%M',
                hour: '%m/%d/%Y/<br> %H:%M',
                day: '%m/%d/%Y<br>',
                week: '%m/%d/%Y<br> ',
                month: '%m/%Y ',
                year: '%Y '
            },
            min: startdate,
            max: stopdate,
            labels: {
                style: {
                    fontSize: font,
                    color: fontcolor
                },
                rotation: -30
            },
            crosshair: true,
            //gridLineDashStyle: 'dash',
            gridLineWidth: gridwidth,
            gridLineColor: grid,
            tickColor: graphColor,
            tickLength: 8,
            tickWidth: 2,
            plotBands:
            [{
                from: startdate,
                to: stopdate,
                color: graphColor
            }]
        }],

        yAxis:
        [{
            visible: yVisible,
            minRange: 1,
            min: null,
            max: null,
            startOnTick: false,
            endOnTick: false,
            showLastLabel: true,
            tickColor: line1,
            tickLength: 8,
            tickWidth: 2,
            //gridLineDashStyle: 'dash',
            gridLineWidth: gridwidth,
            gridLineColor: grid,
             /*scrollbar: {
                enabled: true,
                showFull: false
            },*/
            labels: {
                format: '{value}' + Y1suffix,
                style: {
                    fontSize: font,
                    color: line1
                }
            },

            crosshair: true,
            opposite: false,
            resize: {
                    enabled: true
                  }
        },
        {
            visible: y2Visible,
            minRange: 1,
            min: null,
            max: null,
            startOnTick: false,
            endOnTick: false,
            showLastLabel: true,
            tickColor: '#d1d1d1',
            tickLength: 8,
            tickWidth: 2,
            //gridLineDashStyle: 'dash',
            gridLineWidth: gridwidth,
            gridLineColor: grid,
            /* scrollbar: {
                enabled: true,
                showFull: false
            },*/
            labels: {
                format: '{value}' + axisY2suffix,
                style: {
                    fontSize: font,
                    color: '#d1d1d1'
                }
            },
            opposite: true,
             resize: {
                    enabled: true
            }

        }],

        series: value,

        plotOptions: plotOptions
    });
    setLimits();
    setPlotband1(jsonfile);

    enableButtons();
}

//---------------------------------------------------------------------------------//
/*
setTheme(num) - sets color of the graph

graphBackground
*/

function setTheme(num)
{
    //dark
    if( num == 0)
    {
        graphBackground = '#3d3a3a';
        graphBorder = '#8e8b8b';
        grid = '#8e8b8b';
        font = '#e8edf4';
        Upperline1 = '#a0a0a0';
        line1= '#6ab712';
        Lowerline1 = '#a0a0a0';
        Upperline2 = '#a0a0a0';
        line2 = '#ffd530';
        Lowerline2 = '#a0a0a0';
        linewidth = 1.8;
        gridwidth = 0.5;
        graphColor = '#5e5959';
        UpperLimitLine = '#ededed';
        UpperLimitArea = '#ff6060';
        BelowLimitLine = '#bfbfbf';
        BelowLimitArea = '#7fb9e2';
        WithinLimitArea = '#5e5959';
    }

    //light
    if( num == 1)
    {
        graphBackground = '#fdfdfd';
        graphBorder = '#d1d1d1';
        graphColor = '#fdfdfd';
        grid = '#d1d1d1';
        fontcolor = '#4c4c4c';
        font = '1.5vh';
        Upperline1 = '#017500';
        line1= '#96c695';
        Lowerline1 = '#017500';
        Upperline2 = '#602999';
        line2=  '#d8b2ff';
        Lowerline2 = '#602999';
        linewidth = 1.8;
        gridwidth = 0.5;
        UpperLimitLine = '#d8b2ff';
        BelowLimitLine = '#6d996c';
        WithinLimitArea = '#f8f8f8';
    }
}
//---------------------------------------------------------------------------------//



//---------------------------------------------------------------------------------//
function enableButtons()
{
    document.getElementById("ZoomInY").addEventListener("mouseup", function()
    {
        if(chart.yAxis[0].min + zoomyInterval < chart.yAxis[0].max - zoomyInterval)
            chart.yAxis[0].setExtremes(chart.yAxis[0].min + zoomyInterval,chart.yAxis[0].max - zoomyInterval, false, false)

        if(chart.yAxis[1].min + zoomy2Interval < chart.yAxis[1].max - zoomy2Interval)
            chart.yAxis[1].setExtremes(chart.yAxis[1].min + zoomy2Interval,chart.yAxis[1].max - zoomy2Interval, true, false)
    });

    document.getElementById("ZoomOutY").addEventListener("mouseup", function()
    {
        if(chart.yAxis[0].min > minY - zoomyInterval)
            chart.yAxis[0].setExtremes(chart.yAxis[0].min - zoomyInterval, chart.yAxis[0].max + zoomyInterval, false, false)

        if(chart.yAxis[1].min > minY2 - zoomy2Interval)
            chart.yAxis[1].setExtremes(chart.yAxis[1].min - zoomy2Interval,chart.yAxis[1].max + zoomy2Interval, true, false)
    });

    document.getElementById("ZoomInX").addEventListener("mouseup", function()
    {
        if(chart.xAxis[0].min + zoomxInterval*1.5 < chart.xAxis[0].max - zoomxInterval)
            chart.xAxis[0].setExtremes(chart.xAxis[0].min + zoomxInterval,chart.xAxis[0].max - zoomxInterval, true, false);
    });

    document.getElementById("ZoomOutX").addEventListener("mouseup", function()
    {
        if((chart.xAxis[0].min > startdate) || (chart.xAxis[0].max < stopdate))
            chart.xAxis[0].setExtremes(chart.xAxis[0].min - zoomxInterval,chart.xAxis[0].max + zoomxInterval, true, false);
    });

    document.getElementById("RESET").addEventListener("mouseup", function()
    {
        chart.xAxis[0].setExtremes(startdate, stopdate, false, false)
        chart.yAxis[0].setExtremes(minY - zoomyInterval, maxY + zoomyInterval, false, false)
        chart.yAxis[1].setExtremes(minY2 - zoomy2Interval, maxY2 + zoomy2Interval, true, false)
    });

    document.getElementById("showLimits").addEventListener("mouseup", function()
    {
        createJSON();
        chart.yAxis[0].setExtremes(TLL - zoomyInterval/2, TUL + zoomyInterval/2, false, false)
        chart.yAxis[1].setExtremes(HLL - zoomy2Interval, HUL + zoomy2Interval, true, false)
    });

    document.getElementById("HideGray").addEventListener("click", function()
    {
        var hidetraces = document.querySelector('.hideAllTraces').checked;
        for(var i = 0; i< chart.series.length ; i++)
       {
            if(chart.series[i].color == '#bababa')
           {
                if(hidetraces == true)
                    chart.series[i].hide();
                else
                    chart.series[i].show();

                break;
            }
       }

    });

    document.getElementById("ShowLimit").addEventListener("click", function()
    {
        for(var i = 0; i< chart.series.length ; i++)
        {
            if(document.querySelector('.showAllLimit').checked == true)
            {
                if(chart.series[i].color != '#bababa')
                {
                    if(i== 0)
                        setPlotband1(jsonfile);
                    if(i == 1)
                        setPlotband2(jsonfile);
                }
            }
            else
            {
                if(visible == 1)
                {
                    if(chart.series[i].color == '#bababa')
                    {
                        if(i== 0)
                            chart.yAxis[0].removePlotBand('CH0');
                        if(i == 1)
                            chart.yAxis[1].removePlotBand('CH1');
                    }
                }
                else
                {
                    chart.yAxis[0].removePlotBand('CH0');
                    chart.yAxis[1].removePlotBand('CH1');
                }
            }
        }

    });

    document.getElementById("showSelectSamples").addEventListener("mouseup", function()
    {
         chart.xAxis[0].setExtremes(selectedstartdate, selectedstopdate);
    });


    //disables ctrl+scroll
    document.onmousewheel = function(){ stopWheel(); }
    if(document.addEventListener){
        document.addEventListener('DOMMouseScroll', stopWheel, false);
    }
     
    function stopWheel(e){
        if(!e){ e = window.event; }
        if(e.preventDefault) { e.preventDefault(); }
        e.returnValue = false;
    }


    document.onclick = hideContextMenu;
    var contextMenu = document.getElementById('contextMenu');
    var commentbox = document.getElementById('box');
    var cancelbox = document.getElementById('cancel');
    function showContextMenu(event)
    {
        contextMenu.style.display = 'block';
        contextMenu.style.left = event.clientX + 'px';
        contextMenu.style.top = event.clientY + 'px';
        return false;
    }

    function hideContextMenu ()
    {
        contextMenu.style.display = 'none' ;
    }


    document.getElementById("SetStart").addEventListener("mouseup", function()
    {
        selectedstartdate = timestamp;

        chart.xAxis[0].removePlotBand('SetStart');

        chart.xAxis[0].addPlotBand
        ({
            id:'SetStart',
            from: timestamp,
            to: startdate,
            color:  'rgba(112,112,112,0.5)',
            zIndex: 4
        });

        chart.xAxis[0].addPlotLine
        ({ //lower limit
            id:'SetStart',
            width: 1.5,
            value: timestamp,
            color: "gray",
            dashStyle: 'dash',
            zIndex: 4
        });
    });

    document.getElementById("SetEnd").addEventListener("mouseup", function()
    {
        selectedstopdate = timestamp;

        chart.xAxis[0].removePlotBand('SetEnd');

        chart.xAxis[0].addPlotBand
        ({
            id:'SetEnd',
            from: timestamp,
            to: stopdate,
            color:  'rgba(112,112,112,0.5)',
            zIndex: 4
        });



        chart.xAxis[0].addPlotLine
        ({ //lower limit
            id:'SetEnd',
            width: 1.5,
            value: timestamp,
            color: "gray",
            dashStyle: 'dash',
            zIndex: 4
        });
    });


    document.getElementById("ClearStart").addEventListener("mouseup", function()
    {
        selectedstartdate = startdate;
        chart.xAxis[0].removePlotBand('SetStart');
    });


    document.getElementById("ClearEnd").addEventListener("mouseup", function()
    {
        selectedstopdate = stopdate;
        chart.xAxis[0].removePlotBand('SetEnd');
    });
        
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function setLimits ()
{
    minY = chart.yAxis[0].dataMin;
    maxY = chart.yAxis[0].dataMax;
    minY2 = chart.yAxis[1].dataMin;
    maxY2 = chart.yAxis[1].dataMax;
    zoomxInterval = (stopdate-startdate)/20;
    zoomyInterval = (maxY - minY)/10;
    zoomy2Interval = (maxY2 - minY2)/10;
    selectedstartdate = chart.xAxis[0].dataMin;
    selectedstopdate = chart.xAxis[0].dataMax;

    chart.update({
        yAxis:
         [{
            min: minY - zoomyInterval,
            max: maxY + zoomyInterval
        },
        {
            min: minY2 - zoomy2Interval,
            max: maxY2 + zoomy2Interval
        }]
    });


    if ((minY < TLL) || (maxY > TUL) || (minY2 < HLL) || (maxY2 > HUL))
    {
        var image = document.getElementById("image");
        image.src = "images/redWarning.png"
    }

    if(jsonfile.device[0].zoom != undefined)
    {
        var zoom = jsonfile.device[0].zoom;
        chart.xAxis[0].setExtremes(zoom.startdate,zoom.stopdate);
        chart.yAxis[0].setExtremes(zoom.y0min, zoom.y0max);
        chart.yAxis[0].setExtremes(zoom.y1min, zoom.y1max);

        document.querySelector('.hideAllTraces').checked = zoom.hidegray;
        document.querySelector('.hideAllTraces').checked = zoom.showalllimits;
    }

}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function setAppSetting(dataRecieve)
{
    document.getElementById("zoom").style.display = 'none';
    //AM/PM FORMAT PR 24HRs
    if(dataRecieve.device[0].header.apmp == "true")
    {
        hour = "hh"; //"hh" for 12hrs
        timesuffix = "TT"; // for AM
    }
    else
    {
        hour = "HH";
        timesuffix = "";
    }

    axisY1suffix = dataRecieve.device[0].header.mainyaxis;
    Y1suffix = dataRecieve.device[0].header.mainyaxis;
    navigatorenable = false;
    plotOptions = {};
    initialY2 = line2;
    initialY2Limit = Upperline2;
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function setSettings(dataRecieve)
{
    //AM/PM FORMAT PR 24HRs
    if(dataRecieve.device[0].header.apmp == "true")
    {
        hour = "%h"; //"hh" for 12hrs
        timesuffix = "%T"; // for AM
    }
    else
    {
        hour = "%H";
        timesuffix = "";
    }

    axisY1suffix = dataRecieve.device[0].header.mainyaxis;
    Y1suffix = dataRecieve.device[0].header.mainyaxis;

    if((axisY1suffix.charAt(2) + axisY1suffix.charAt(3)) == "\xB0C")
        Y1suffix = "\xB0C";

    plotOptions = {
                    series: {
                        stickyTracking: true,
                        showInNavigator: true,
                        allowPointSelect: true,
                        cursor: 'pointer',
                        marker: {
                            enabled: true,
                            radius: 1
                        },
                        point: {
                            events: {
                                click: function(e) {
                                    var lineinfo = this;
                                    eventflag = true;
                                    clicknumber++;
                                    if(clickDetected) {
                                        if(e.point.options.title == undefined){
                                            commentbox.style.display = 'block';
                                            Xinfo = lineinfo.x;
                                            indexcolor = lineinfo.color;
                                        }

                                        else if(e.point.options.title == 'COMMENT'){
                                            cancelbox.style.display = 'block';
                                            removeX = lineinfo.x;
                                        }
                                            clickDetected = false;
                                    }
                                    else {
                                        clickDetected = true;
                                        setTimeout(function() {
                                            clickDetected = false;
                                            if(clicknumber == 1){
                                                pointclickstate(lineinfo.series);
                                                clicknumber = 0;
                                            }
                                        }, 250);
                                    }
                                ;},
                                mouseOut: function () {
                                    timestamp = this.x;
                                }
                            }
                        },
                    events: {
                        mouseOver: function ()
                        {
                            if(eventflag == false)
                            {
                                if(this.index == 0)
                                {
                                    chart.series[0].update({
                                        zones:[{value: TLL,color: Lowerline1},{value: TUL,color:line1},{color: Upperline1}]
                                    });

                                    chart.yAxis[0].update({
                                        labels:{
                                            style: {
                                                color: line1
                                            }
                                        }
                                    });

                                    chart.yAxis[0].update({
                                        tickColor: line1
                                    });
                                }

                                if(this.index == 1)
                                {
                                    chart.series[1].update({
                                        zones:[{value: HLL, color: Lowerline2},{value: HUL, color: line2},{color: Upperline2}]
                                    });

                                    chart.yAxis[1].update({
                                        labels:{
                                            style: {
                                                color: line2
                                            }
                                        }
                                    });

                                    chart.yAxis[1].update({
                                        tickColor: line2
                                    });
                                }
                            }
                        },
                        mouseOut: function ()
                        {

                        if(eventflag == false)
                        {
                            trace = this; 

                            if(trace.color === '#bababa')
                            { 

                                chart.series[trace.index].update({
                                    zones:[{color: '#d1d1d1'},{color: '#d1d1d1'},{color: '#d1d1d1'}]
                                });

                                chart.yAxis[trace.index].update({
                                    labels:{
                                        style: {
                                            color: '#d1d1d1'
                                        }
                                    }
                                });

                                chart.yAxis[trace.index].update({
                                        tickColor: '#d1d1d1'
                                    })

                                if(visible == 0)
                                {
                                    chart.yAxis[0].removePlotBand('CH0');
                                    chart.yAxis[1].removePlotBand('CH1');
                                };
                            }

                            else
                            {
                                showLimit(trace);
                            }
                         }
                     },
                        legendItemClick: function () {clickstate(this);}
                    }

                }
            };

    initialY2 = '#bababa';
    initialY2Limit = '#bababa';

}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function getData(dataRecieve)
{
    var datetime = [];
    var value = [];
    var valuesize;
    var channelsize; 
    var dataPoint = []; //set of details, unique for each trace

    datetime = dataRecieve.device[0].header.datetime;
    channelsize = Object.keys(dataRecieve.device[0].channel).length;

    //need devicesize and a way to deal with multiple files
    for(var h = 0; h < channelsize; h++)
    {
        if(dataRecieve.device[0].channel[h].units == "")
            tag = h;

        value = dataRecieve.device[0].channel[h].value;
        valuesize = Object.keys(dataRecieve.device[0].channel[h].value).length;

        if( h != tag)
        {
            if(dataRecieve.device[0].channel[h].units == axisY1suffix)
            {
                yVisible = true;
                visible++;
                if(dataRecieve.device[0].channel[h].limits.lower.mid[0] != undefined)
                    TLL = parseFloat(dataRecieve.device[0].channel[h].limits.lower.mid[0]);
                if(dataRecieve.device[0].channel[h].limits.upper.mid[0] != undefined)
                    TUL = parseFloat(dataRecieve.device[0].channel[h].limits.upper.mid[0]);

                dataPoint[h] =
                {
                    id: dataRecieve.device[0].header.serial + Y1suffix,
                    name: Y1suffix,
                    color: line1,
                    lineWidth: linewidth,
                    data: [],
                    zones: [{
                        value: TLL,      //< -10
                        color: Lowerline1
                    }, {
                        value: TUL,       // < 10
                        color: line1 //yellow
                    }, {
                        color: Upperline1
                    }],

                    yAxis: 0
                }
            }
            else
            {
                if(dataRecieve.device[0].channel[h].limits.lower.mid[0] != undefined)
                    HLL = parseFloat(dataRecieve.device[0].channel[h].limits.lower.mid[0]);
                if(dataRecieve.device[0].channel[h].limits.upper.mid[0] != undefined)
                    HUL = parseFloat(dataRecieve.device[0].channel[h].limits.upper.mid[0]);

                y2Visible = true;
                axisY2suffix =  dataRecieve.device[0].channel[h].units;
                if(visible > 0)
                {

                    dataPoint[h] =
                    {
                        id: dataRecieve.device[0].header.serial + axisY2suffix,
                        name:axisY2suffix,
                        color: '#bababa',
                        lineWidth: linewidth,
                        data: [],
                        zones: [{
                            value: HLL,
                            color: initialY2Limit
                        }, {
                            value: HUL,
                            color: initialY2
                        }, {
                            color: initialY2Limit
                        }],

                        yAxis: 1
                    }
                }

                else
                {
                    visible++;
                    dataPoint[h] =
                    {
                        id: dataRecieve.device[0].header.serial + axisY2suffix,
                        name: axisY2suffix,
                        color: '#bababa',
                        lineWidth: linewidth,
                        data: [],
                        zones: [{
                            value: HLL,
                            color: Lowerline2
                        }, {
                            value: HUL,
                            color: line2
                        }, {
                            color: Upperline2
                        }],

                        yAxis: 1
                    }

                }
            }
        }
        else
        {
            dataPoint[h] =
            {
                id: 'TAG',
                name: 'TAG',
                type: 'flags',
                data:[],
                shape: 'squarepin', //'url(burger.png)'
                color: '#ffa500',
                fillColor: '#ffa500',
                style: {
                color: 'white'
                }
            };
        }

        for (var i = 0; i < valuesize; i++)
        {
            if( h != tag)
            {
                var x = Number(datetime[i])*1000;
                var y = parseFloat(value[i]);
                dataPoint[h].data.push([x,y]);

                if( i == 0)
                    startdate = x;
                if( i == (valuesize -1))
                    stopdate = x;
            }
            else
            {
                if(value[i] == 'true')
                {
                    var newTag =
                    {
                        x: Number(datetime[i])*1000,
                        title:'TAG',
                        text: 'CH0 : ' + dataRecieve.device[0].channel[0].value[i] + Y1suffix + '<br> CH1 : ' + dataRecieve.device[0].channel[1].value[i] + axisY2suffix
                    }

                    dataPoint[h].data.push(newTag);
                }
            }
        }
    }

    return dataPoint;

}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function setPlotband1 (dataRecieve)
{
    var channelsize = Object.keys(dataRecieve.device[0].channel).length;

    for(var a = 0; a < channelsize; a++)
    {
        if(a != tag)
        {
            if(dataRecieve.device[0].channel[a].units == axisY1suffix)
            {
                var limits = dataRecieve.device[0].channel[a].limits;
                if(limits != undefined)
                {
                    chart.yAxis[0].addPlotLine
                    ({ //lower limit
                        id:'CH0',
                        width: 1.5,
                        value: TLL,
                        color: line1,
                        dashStyle: 'dash',
                        label: {
                            text: "LOWER LIMIT : " + TLL + Y1suffix,
                            align: 'right',
                            x: -5,
                            y: -5,
                            style: {
                                color: line1,
                                 fontSize: font
                            }
                        },
                        zIndex: 2
                    });

                    chart.yAxis[0].addPlotBand
                    ({
                        id:'CH0',
                        from: TLL,
                        to: TUL,
                        color: WithinLimitArea
                    });

                    chart.yAxis[0].addPlotLine
                    ({ //upper limit
                        id:'CH0',
                        width: 1.5,
                        value: TUL,
                        color: line1, //green
                        dashStyle: 'dash',
                        label: {
                            text: "UPPER LIMIT : " + TUL + Y1suffix,
                            style: {
                                color: line1, //green
                                 fontSize: font
                            },
                            align: 'right',
                            x: -5,
                            y: -5
                        },
                        zIndex: 2
                    });

                    break;
                }
            }
        }
    }
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function setPlotband2 (dataRecieve)
{
    var channelsize;
    channelsize = Object.keys(dataRecieve.device[0].channel).length;

    for(var b= 0; b < channelsize; b++)
    {
        if(b != tag)
        {
            if(dataRecieve.device[0].channel[b].units != axisY1suffix)
            {
                var limits = dataRecieve.device[0].channel[b].limits;
                if(limits != undefined)
                {

                    chart.yAxis[1].addPlotLine
                    ({ //lower limit
                        id:'CH1',
                        width: 1.5,
                        value: HLL,
                        color: line2,
                        dashStyle: 'dash',
                        label: {
                            text: "LOWER LIMIT : " + HLL + axisY2suffix,
                            style: {
                                color: line2,
                                 fontSize: font
                            },
                            align: 'right',
                            x: -10,
                            y: -5
                        },
                        zIndex: 2
                    });

                    chart.yAxis[1].addPlotBand
                    ({ //upper limit
                        id:'CH1',
                        from: HUL,
                        to: HLL,
                        color: WithinLimitArea
                    });

                    chart.yAxis[1].addPlotLine
                    ({ //lower limit
                        id:'CH1',
                        width: 1.5,
                        value: HUL,
                        color: line2,
                        dashStyle: 'dash',
                         label: {
                            text: "UPPER LIMIT : " + HUL + axisY2suffix,
                            style: {
                                color: line2,
                                 fontSize: font
                            },
                            align: 'right',
                            x: -10,
                            y: -5
                        },
                        zIndex: 2

                    });

                    break;
                }
            }
        }
    }
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function clickstate(line){
    if(line.index != tag)
    {
        if(line.visible == true)
        {
            if(line.color == '#bababa')
            {
                visible++;
                select(line.index);
                showLimit(line);
                line.setVisible();
            }
            else
            {
                visible--;
                deselect(line.index);
                line.setVisible();
                if(document.querySelector('.hideAllTraces').checked == true)
                    line.show();
            }
        }
        else
        {
            visible++;
            select(line);
            showLimit(line);
        }
    }
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function pointclickstate(line){
    if((line.index != tag) && (line.name != 'COMMENT'))
    {
        if(line.visible == true)
        {
            if(line.color == '#bababa')
            {
                visible++;
                select(line.index);
                showLimit(line);
            }
            else
            {
                visible--;
                deselect(line.index);
                if(document.querySelector('.hideAllTraces').checked == true)
                    line.hide();
            }
        }
    }

    eventflag = false;
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function showLimit(index)
{
    if(document.querySelector('.showAllLimit').checked == true)
    {
        if(index == 0)
            setPlotband1(jsonfile);
        if(index == 1)
            setPlotband2(jsonfile);
    }
    else
    {
        if(visible == 1)
        {
            if(index == 0)
                setPlotband1(jsonfile);
            if(index == 1)
                setPlotband2(jsonfile);
        }
        else
        {
            chart.yAxis[0].removePlotBand('CH0');
            chart.yAxis[1].removePlotBand('CH1');
        }
    }
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function select(index)
{
    if(index == 0)
    {
        chart.update({
            series:[{
                zones:[{value: TLL,color: Lowerline1},{value: TUL,color:line1},{color: Upperline1}],
                width: 2,
                color: line1},{}],
            yAxis:[{
                labels:{
                    style: {
                        color: line1
                    }
                },
                tickColor: line1},{}]
        });
    }

    if(index == 1)
    {
        chart.update({
            series:[{},{
                zones:[{value: HLL,color: Lowerline2},{value: HUL,color:line2},{color: Upperline2}],
                width: 2,
                color: line2}],
            yAxis:[{},{
                labels:{
                    style: {
                        color: line2
                    }
                },
                tickColor: line2}]
        });
    }
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function deselect(index)
{
    if(index == 0)
    {
        chart.update({
            series:[{
                zones:[{color: '#d1d1d1'},{color: '#d1d1d1'},{color: '#d1d1d1'}],
                width: 3,
                color: '#bababa'
            },{}],
            yAxis:[{
                labels:{
                    style: {
                        color: '#bababa'
                    }
                },
                tickColor: '#bababa'},{}]
        });

        chart.yAxis[0].removePlotBand('CH0');
    }

    if(index == 1)
    {
        chart.update({
            series:[{},{
                zones:[{color: '#d1d1d1'},{color: '#d1d1d1'},{color: '#d1d1d1'}],
                width: 3,
                color: '#bababa'
            }],
            yAxis:[{},{
                labels:{
                    style: {
                        color: '#bababa'
                    }
                },
                tickColor: '#bababa'}]
        });

         chart.yAxis[1].removePlotBand('CH1');
    }

    if(visible == 1)
    {
        if(index == 0)
            setPlotband2(jsonfile);
        if(index == 1)
            setPlotband1(jsonfile);
    }
}
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function addComment()
{
    commentbox.style.display = 'none';

    var username = document.getElementById("username").value;
    var comment = document.getElementById("comment").value;

    if(comment.length >  0){
        var tagSeries =
            {
                id:'COMMENT' + chart.series.length,
                name: 'COMMENT',
                type: 'flags',
                data:[{
                    x: Xinfo,
                    title: 'COMMENT',
                    text: '<b>USER:</b> ' + username +  '<br>' + '<b>COMMENT: </b>'+  comment + ' <br>'
                 }],
                //onSeries: value[index].name,
                shape: 'squarepin',
                color: indexcolor,
                fillColor: indexcolor,
                style: {
                color: 'white'
                }
            };

        value[value.length] = tagSeries;
        value[value.length-1].linkedTo = 'TAG'

         chart.addSeries(tagSeries);
    };

    chart.update({
        series:value[value.length-1]
    });

    cancel();

};
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function deleteComment()
{
    cancelbox.style.display = 'none';
    document.getElementById("username").value = "";
    document.getElementById("comment").value = "";

    for (var a = 0; a < chart.series.length; a++)
    {
            if(chart.series[a].name == "COMMENT")
            {
                if(chart.series[a].data[0].x == removeX)
                {
                    value.splice(a,1);
                    chart.series[a].remove();
                }

                break;
            }
    }
};
//---------------------------------------------------------------------------------//


//---------------------------------------------------------------------------------//
function cancel()
{
    cancelbox.style.display = 'none';
    commentbox.style.display = 'none';
    document.getElementById("username").value = "";
    document.getElementById("comment").value = "";
};
//---------------------------------------------------------------------------------//



//---------------------------------------------------------------------------------//
function createJSON ()
{
    var channel = 0
    var buildJson = {};
    buildJson["header"] = {};
    buildJson["channel"] = [{}];
    buildJson["zoom"] = {
       "startdate":startdate,
       "stopdate":stopdate,
       "selectedstartdate":selectedstartdate,
       "selectedstopdate":selectedstopdate,
       "xviewmin":chart.xAxis[0].min,
       "xviewmax":chart.xAxis[0].max,
       "y0min":chart.yAxis[0].min,
       "y0max":chart.yAxis[0].max,
       "y1min":chart.yAxis[1].min,
       "y1max":chart.yAxis[1].max,
       "hidegray": document.querySelector('.hideAllTraces').checked,
       "showalllimtis": document.querySelector('.showAllLimit').checked
    };  


    for(var i = 0; i< chart.series.length/2 ; i++)
   {
        channel =
        {
            "name": chart.series[i].name,
            "chstate": chart.series[i].color,
            "value": chart.series[i].data
        }

        buildJson["channel"].push(channel);
   }

    var addheader = {"device":[buildJson]}
    sendJson = JSON.stringify(addheader);
    alert(sendJson); 
}
//---------------------------------------------------------------------------------//
