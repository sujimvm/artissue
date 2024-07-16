$(document).ready(function () {
    const endDate = new Date($("#endDate").val());
    endDate.setDate(endDate.getDate()-1);

    let y = endDate.getFullYear();
    let m =  ( (endDate.getMonth()+1) <= 9 ? "0" + (endDate.getMonth()+1) : (endDate.getMonth()+1) );
    let d = ( (endDate.getDate()) <= 9 ? "0" + (endDate.getDate()) : (endDate.getDate()) );

    $("#cancelDate").val(y+'-'+m+'-'+d);


});