function callAjax(value, row) {

    $.get('StudentListServlet', {"groupId": value},
        function (resp) { // on sucess
            // We need 2 methods here due to the different ways of
            // handling a JSON object.
            printStuds(resp,row);
        })
        .fail(function () { // on failure
            alert("Request failed.");
        });
};

function printStuds(resp,row) {
    // First empty the <div> completely and add a title.
    $("#stud-results"+row).empty()
        .append(resp);
};
