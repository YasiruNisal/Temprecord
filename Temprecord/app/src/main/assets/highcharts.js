
var dataPoint = [];
var value;
var UL = 0;
var LL = 0;
var hour = "hh"; // hh for 12hr && HH for 24hr
var timesuffix = "TT"; // TT for AM/PM &&
var axisY1suffix = '';
var axisY2suffix = '';
var chart;
var loggerData;
var tag = 100;
var clickLine;
var plotband1;
var plotband2;

//gets JSON file format
window.onload = function()
{
    fromJava = android.getData();
    loggerData = JSON.parse(fromJava);
    setSettings(loggerData);
    value = getData(loggerData);
    plotband1 = setPlotband1(loggerData);
    //plotband2 = setPlotband2(loggerData);
    chart = Highcharts.stockChart('container', {

        rangeSelector: {
            enabled: false,
        },

        navigator: {
            height: 40
        },

        chart: {
            type: 'line',
            //zoomType: 'xy',
             backgroundColor: '#2b2e30',
             style: {
                textTransform: 'uppercase',
                fontFamily: '\'Unica One\', sans-serif',
            },
        },
        title: {
            text: 'GRAPH',
             style: {
                color: '#f9fbff',
                fontSize: '12px'
            }
        },

        tooltip: {
            //shared: true,
             split: true,
            crosshairs: true,
            valueDecimals: 2,
        },

        legend: {
            enabled: true,
            align: 'center',
            backgroundColor: 'none',
            layout: 'horizontal',
            verticalAlign: 'top',
            itemStyle: {
                color: '#d0d5e0',
            },
            itemStyle: {
                fontSize: '8px',
                color: '#e8edf4'
            },
            borderWidth: 0
        },



        xAxis: {
            type: 'datetime',
            labels: {
                style: {
                    fontSize: '10px',
                    color: 'white'
                }
            }
        //crosshair: true,
        //gridLineWidth: 1,
        //gridZIndex: 1
        },

        yAxis: 
        [{
            labels: {
                style: {
                    fontSize: '10px',
                    color: 'white'
                }
            },
            //title: {
            //    text:'Temperature',
            //    style:{
            //        fontSize: '10px'
            //    }
            //},
            //crosshair: true,
            //gridLineWidth: 1,
            //gridZIndex: 1,
            opposite: false,
            resize: {
                    enabled: true
                  },
            plotBands: plotband1,
        },

        {
            labels: {
                    style: {
                        fontSize: '10px',
                        color: 'white'
                    }
                },
            opposite: true,
             resize: {
                    enabled: true
            },

            plotBands: plotband2,
        
        }],

        series: value,

         plotOptions: {
                series: {
                    showInNavigator: true,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function (e) {
                                console.log(e.target);
                                if(e.target.innerHTML == ''){
                                    getnewTag(this.x, this.y);   
                                }

                                else if(e.target.innerHTML == 'COMMENT'){
                                    removeTag(this.x);
                                }
                            }
                        }
                    },
                    events: {
                            click: function (e) {
                                console.log(this.name);
                                clickLine = this.name;
                            }
                        }
                },
                marker: {
                    lineWidth: 1
                }
            },
        
    });
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
}


function getnewTag(newX, newY)
{
        var newComment = prompt("COMMENT:",)
        
        //Temp[j-1].onSeries = clickLine; For this we need to make a new set of dataPoints
        if(newComment.length >  0){
            Temp[dataPoint.length-1].data.push({
            x: newX,
            title: 'COMMENT',
            text: '<b>USER:</b> ' + 'JiMin <br>' + '<b>COMMENT: </b>'+  newComment + ' <br>' + '<b> TEMP: </b>' + parseFloat(newY).toFixed(2) + tempsuffix,
             })
        }

    chart.update({
        series:Temp
    });

    console.log(Temp);
}

function removeTag(removeX)
{
    var remove = confirm('Do you want to remove this Comment? ')
    console.log(remove);

    if(remove == true)
    {
        for (var a = 0; a < Temp[dataPoint.length-1].data.length; a++)
        { 
            if(Temp[dataPoint.length-1].data[a].x == removeX)
                chart.series[dataPoint.length-1].data[a].remove();
        }
    }
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
    console.log(datetime);
    channelsize = Object.keys(dataRecieve.device[0].channel).length;
    for(var c = 0; c < channelsize; c++)
    {
        if(dataRecieve.device[0].channel[c].units == '')
            tag = c;
    }

    //need devicesize and a way to deal with multiple files 
    for(var h = 0; h < channelsize; h++)
    {
        if( h != tag)
        {
            value = dataRecieve.device[0].channel[h].value;
            valuesize = Object.keys(dataRecieve.device[0].channel[h].value).length;  
            dataPoint[h] =
            {
                id: dataRecieve.device[0].header.serial,
                name: dataRecieve.device[0].header.serial,
                data: [],
                marker: {
                    symbol: 'diamond',
                    size:10
                },
                zones: [{
                    value: parseFloat(dataRecieve.device[0].channel[h].limits.lower.mid[0]),      //< -10
                    color: '#96beff' //blue
                }, {
                    value: parseFloat(dataRecieve.device[0].channel[h].limits.upper.mid[0]),       // < 10
                    color: '#c9c9c9' //black
                }, {
                    color: '#ffd747' //yellow
                }],

                yAxis: 0
            }

            for (var i = 0; i < valuesize; i++)
            {

                x = parseInt(datetime[i]);
                y = parseFloat(value[i]);

                dataPoint[h].data.push([x,y]);
            }
        }
        else
        {
            dataPoint[h] =
            {
                name: 'TAG',
                type: 'flags',
                data:[],
                onSeries: dataRecieve.device[0].header.serial,
                shape: 'squarepin',
                color: Highcharts.getOptions().colors[0],
                fillColor: Highcharts.getOptions().colors[0],
                style: {
                color: 'white'
                },
            }

            for (var j = 0; j < valuesize; j++)
            {
                if(value[i] == 'true')
                {
                     
                    var newTag =
                    {
                        x: parseInt(time[i]),
                        title:'TAG',
                        text: '"TAGGED" <br>"' + "TEMP: " + temp[i] + '\xB0C'
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
    var plotband = [];
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
                    plotband.push = 
                    ({ //lower limit
                        from: parseFloat(limits.lower.mid[0]),
                        to: parseFloat(limits.lower.mid[0]) - 30,
                        color: 'rgba(33, 158, 255, 0.1)',
                        label: {
                            text: 'Below Lower Limit',
                            align: 'right',
                            style: {
                                color: '#96beff', //blue
                                 fontSize: '5px'
                            }
                        },
                    },
                    { //upper limit
                        from: parseFloat(limits.upper.mid[0]),
                        to: parseFloat(limits.upper.mid[0]) + 30,
                        color: 'rgba(255, 188, 20, 0.1)',
                        label: {
                            text: 'Above Lower Limit',
                            style: {
                                color: '#ffd747', //yellow
                                 fontSize: '8px'
                            },
                            align: 'right',
                        }
                    });
                }
            }
        }
    }
    return plotband;
}

function setPlotband2 (dataRecieve)
{
    var plotband = []; 
    var channelsize = Object.keys(dataRecieve.device[0].channel).length;

var stripline = [];
    var channelsize = Object.keys(dataRecieve.device[0].channel).length;

    for(var b= 0; b < channelsize; b++)
    {
        if(b != tag)
        {
            if(dataRecieve.device[0].channel[b].units != axisY1suffix)
            {

                axisY2suffix = dataRecieve.device[0].channel[b].units;
                var limits = dataRecieve.device[0].channel[b].limits;
                if(limits != undefined)
                {
                    plotband.push = 
                    ({ //lower limit
                        from: 0,
                        to: parseFloat(limits.lower.mid[0]),
                        color: 'rgba(33, 158, 255, 0.1)',
                        label: {
                            text: 'Below Lower Limit',
                            style: {
                                color: '#96beff', //blue
                                 fontSize: '10px'
                            },
                            align: 'right',
                            x: 10,
                            y: 0,
                        }
                    },
                    { //upper limit
                        from: parseFloat(limits.upper.mid[0]),
                        to: 100,
                        color: 'rgba(255, 188, 20, 0.1)',
                        label: {
                            text: 'Above Lower Limit',
                            style: {
                                color: '#ffd747', //yellow
                                 fontSize: '10px'
                            },
                            align: 'right',
                            x: 10,
                            y: 0,
                        }
                    });
                }
            }
        }
    }
    return plotband2;
}