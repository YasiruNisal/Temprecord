"use strict";
var dataPoint = [];
var value;
var hour = "hh"; // hh for 12hr && HH for 24hr
var timesuffix = "TT"; // TT for AM/PM &&
var axisY1suffix = '';
var axisY2suffix = '';
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
var fromJava = 0;
var deviceinfo = 0;

//gets JSON file format
window.onload = function()
{
    setTheme(1);
    fromJava = android.getData();
    deviceinfo = JSON.parse(fromJava);
    setSettings(deviceinfo);
    value = getData(deviceinfo);
    chart = Highcharts.stockChart('container', {

        //time: {
        //    timezone: 'Europe/London'
        //},

        rangeSelector: {
            enabled: false
        },

        navigator: {
            enabled: false
        },

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
            shared: true,
            //split: true,
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
                second: '%m/%d/%Y<br/>%H:%M:%S',
                minute: '%m/%d/%Y<br/>%H:%M',
                hour: '%m/%d/%Y/<br/>%H:%M',
                day: '%Y<br/>%m/%d',
                week: '%Y<br/>%m/%d',
                month: '%Y/%m',
                year: '%Y'
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
                    color: line2
                }
            },
            opposite: true,
             resize: {
                    enabled: true
            }

        }],

        series: value,

         plotOptions: {
                series: {
                    stickyTracking: true,
                    showInNavigator: true,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    marker: {
                        enabled: true,
                        radius: 1
                    },
                }
            }
    });
    setLimits();
    setPlotband1(deviceinfo);
}

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

function setTheme(num)
{
    //dark
    if( num == 0)
    {
        graphBackground = '#3d3a3a';
        graphBorder = '#8e8b8b';
        grid = '#8e8b8b';
        font = '#e8edf4';
        Upperline1 = '#a0a0a0'
        line1= '#6ab712';
        Lowerline1 = '#a0a0a0'
        Upperline2 = '#a0a0a0'
        line2 = '#ffd530';
        Lowerline2 = '#a0a0a0'
        linewidth = 0.45;
        graphColor = '#5e5959'
        UpperLimitLine = '#ededed'
        UpperLimitArea = '#ff6060'
        BelowLimitLine = '#bfbfbf'
        BelowLimitArea = '#7fb9e2'
        WithinLimitArea = '#5e5959'
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
        Upperline1 = '#602999'
        line1= '#d8b2ff';
        Lowerline1 = '#602999'
        Upperline2 = '#017500'
        line2=  '#96c695';
        Lowerline2 = '#017500'
        linewidth = 1.8
        gridwidth = 0.5
        UpperLimitLine = '#d8b2ff'
        BelowLimitLine = '#6d996c'
        WithinLimitArea = '#f8f8f8'
    }

    if( num == 2) //YELLOW
    {
        graphBackground = '#ffc738';
        graphBorder = '#fcdd8d';
        grid = '#fcdd8d';
        font = '#6b3412';
        Upperline1 = '#f29674'
        line1= '#ff5a1e';
        Lowerline1 = '#bc3301'
        Upperline2 = '#ff6600'
        line2=  '#843500';
        Lowerline2 = '#381600'
        linewidth = 2;
        graphColor = '#fff0c9'
        UpperLimitLine = '#ff3d3d'
        UpperLimitArea = '#ffdddd'
        BelowLimitLine = '#565454'
        BelowLimitArea = '#ddefff'
    }

    if( num == 3) //PINK
    {
        graphBackground = '#ffe5f4';
        graphBorder = '#f7e3f4';
        grid = '#f7e3f4';
        font = '#9b3372';
        Upperline1 = '#ffaab7'
        line1= '#d677ff';
        Lowerline1 = '#d166a2'
        Upperline2 = '#ef6ebc'
        line2=  '#b2297c';
        Lowerline2 = '#7f1555'
        linewidth = 2;
        graphColor = '#fff7fe'
        UpperLimitLine = '#e293c3'
        UpperLimitArea = '#ffdddd'
        BelowLimitLine = '#96beff'
        BelowLimitArea = '#ddefff'
    }

    if( num == 4) //BLUE
    {
        graphBackground = '#2e67b2';
        graphBorder = '#d8ebff';
        grid = '#d8ebff';
        font = '#f2f9ff';
        Upperline1 = '#6497db'
        line1= '#073675';
        Lowerline1 = '#071528'
        Upperline2 = '#3daaff'
        line2=  '#0076d1';
        Lowerline2 = '#071b75'
        linewidth = 2;
        graphColor = '#b5d1ff'
        UpperLimitLine = '#ff3d3d'
        UpperLimitArea = '#ffdddd'
        BelowLimitLine = '#001838'
        BelowLimitArea = '#ddefff'
    }
}

function setLimits ()
{
    minY = chart.yAxis[0].dataMin;
    maxY = chart.yAxis[0].dataMax;
    minY2 = chart.yAxis[1].dataMin;
    maxY2 = chart.yAxis[1].dataMax;
    zoomxInterval = (stopdate-startdate)/20;
    zoomyInterval = (maxY - minY)/10;
    zoomy2Interval = (maxY2 - minY2)/10;
    selectedstartdate = startdate - zoomxInterval;
    selectedstopdate = stopdate + zoomxInterval;

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
        image.src = "redWarning.png"
    }

}

function setSettings(dataRecieve)
{
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


}

function getData(dataRecieve)
{
    var datetime = [];
    var value = [];
    var valuesize;
    var channelsize;
    var data = [];
    var Color;

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
                        color: line2,
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

                else
                {
                    visible++;
                    dataPoint[h] =
                    {
                        id: dataRecieve.device[0].header.serial + axisY2suffix,
                        name: axisY2suffix,
                        color: line2,
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

var channelsize
function setPlotband2 (dataRecieve)
{

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
