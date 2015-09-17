var hc_chart;
var hc_clicks;
var hc_trackbacks;

$(function () {
    $(document).ready(function () {
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        hc_chart = $('#timeline').highcharts({
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function () {
                        var series0 = this.series[0];
                        setInterval(function () {
                            var x = (new Date()).getTime(), // current time
                                y = hc_clicks;
                            series0.addPoint([x, y], true, true);
                        }, 1000);
                        var series1 = this.series[1];
                        setInterval(function () {
                            var x = (new Date()).getTime(), // current time
                                y = hc_trackbacks;
                            series1.addPoint([x, y], true, true);
                        }, 1000);
                    }
                }
            },
            title: {
                text: 'Global tracking data'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 150
            },
            yAxis: {
                title: {
                    text: 'Click vs Trackback'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' + Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' + Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: 'Click',
                color: '#96F70D',
                data: (function () {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
                    for (i = 0; i < 20; i++) {
                        data.push({
                            x: time + (i - 19) * 1000,
                            y: 0
                        });
                    }
                    return data;
                }())
            }, {
                name: 'Trackback',
                color: '#D21F1F',
                data: (function () {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
                    for (i = 0; i < 20; i++) {
                        data.push({
                            x: time + (i - 19) * 1000,
                            y: 0
                        });
                    }
                    return data;
                }())
            }]
        });
    });
});

function eventStatistics(numClicks, numTrackbacks) {
    hc_clicks = numClicks;
    hc_trackbacks = numTrackbacks;
}