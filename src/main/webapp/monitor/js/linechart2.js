function _createChart(canvasId, drawData){
        var myChart = new JSChart(canvasId, 'bar');
        myChart.setTitle("");
        myChart.setAxisNameX('');
        myChart.setAxisNameY(drawData.axisY);
        myChart.setAxisNameColor('#999');
        myChart.setAxisValuesColor('#777');
        myChart.setAxisColor('#B5B5B5');
        myChart.setAxisWidth(1);
        myChart.setBarValues(false);
        myChart.setBarValuesColor('#2F6D99');
        myChart.setAxisPaddingBottom(20);
        if (drawData.paddingLeft){
           myChart.setAxisPaddingLeft(drawData.paddingLeft);
        }else{
           myChart.setAxisPaddingLeft(40);
        }
        myChart.setDataArray(drawData.data);
        var divide=1;
        if (drawData.data.length==12){
            divide=3;
        }
        for (var i=0;i<drawData.data.length;i++){
           if ((i%divide)==0){
                myChart.setLabelX([i,drawData.data[i][0]]);
           }
           drawData.data[i][0]=i;
        }
        myChart.setShowXValues(false);
        myChart.setAxisValuesNumberX(5);
        myChart.setAxisPaddingTop(5);
        myChart.setAxisNameFontSizeX(6);
        myChart.setTitleFontSize(11);
        myChart.setBarBorderWidth(0);
        myChart.setBarSpacingRatio(50);
        myChart.setBarOpacity(0.9);
        myChart.setFlagRadius(6);
        myChart.setTooltipPosition('nw');
        myChart.setTooltipOffset(3);
        myChart.setSize(340, 150);
        myChart.setGridColor('#C6C6C6');
        return myChart;
}
function draw2Series(canvasId, drawData){
        var myChart = _createChart(canvasId, drawData);
        myChart.setLegendShow(true);
        myChart.setLegendPosition("top");
        myChart.setBarColor('#2D6B96', 1);
        myChart.setBarColor('#9CCEF0', 2);
        myChart.setLegendForBar(1, drawData.series1Title);
        myChart.setLegendForBar(2, drawData.series2Title);
        myChart.draw();
}

function drawLineChart(canvasId, drawData){
        var myChart = new JSChart(canvasId, 'line');
        myChart.setAxisValuesNumberY(5);
        myChart.setIntervalStartY(0);
        myChart.setAxisValuesNumberX(5);
        myChart.setAxisValuesDecimals(0);
        myChart.setAxisNameY(drawData.axisY);
        myChart.setAxisNameX('');
        myChart.setAxisPaddingLeft(57);
        myChart.setAxisPaddingTop(5);
        myChart.setTextPaddingBottom(0);
        myChart.setAxisPaddingRight(0);
        myChart.setAxisNameFontSizeX(6);
        myChart.setTitle('');
        myChart.setAxisValuesNumberX(5);
        var divide;
        if (drawData.data.length==12){
           divide=3;
        }else{
           divide=7;
        }
        for (var i=0;i<drawData.data.length;i++){
           //myChart.setTooltip([i]);
           if ((i%divide)==0){
               myChart.setLabelX([i,drawData.data[i][0]]);
           };
           drawData.data[i][0]=i;
        }
        myChart.setDataArray(drawData.data, 'red');
        myChart.setShowXValues(false);
        myChart.setTitleColor('#454545');
        myChart.setAxisValuesColor('#454545');
        myChart.setLineColor('#A4D314', 'green');
        myChart.setLineColor('#444444', 'gray');
        myChart.setFlagColor('#9D16FC');
        myChart.setFlagRadius(4);
        myChart.setSize(340, 150);
        myChart.draw();
}
function draw1Series(canvasId, drawData){
        var myChart = _createChart(canvasId, drawData);
        myChart.setBarColor('#2D6B96', 1);
        myChart.setLegendForBar(1, drawData.series1Title);
        myChart.draw();
}

function drawPieChart(canvasId, drawData){
        var colors = ['#0673B8', '#0091F1', '#F85900', '#1CC2E6', '#C32121'];
        var myChart = new JSChart(canvasId, 'pie');
        myChart.setDataArray(drawData.data);
        myChart.colorizePie(colors);
        myChart.setTitle('');
        myChart.setTextPaddingTop(280);
        myChart.setPieValuesDecimals(1);
        myChart.setPieUnitsFontSize(9);
        myChart.setPieValuesFontSize(8);
        myChart.setPieValuesColor('#fff');
        myChart.setPieUnitsColor('#969696');
        myChart.setSize(340, 200);
        //myChart.setPiePosition(308, 145);
        myChart.setPieRadius(75);
        myChart.setFlagColor('#fff');
        myChart.setFlagRadius(4);
        myChart.setTooltipOpacity(1);
        myChart.setTooltipBackground('#ddf');
        myChart.setTooltipPosition('ne');
        myChart.setTooltipOffset(2);
        myChart.draw();
}
