$("input[name='name']").keyup(function(event) {


    // Parameters we will need to calculate our suggestions.
    var postParameters = {input: $("input[name='name']").val()};
    console.log(postParameters)
    // console.log(document.getElementById('searchbar_donor').value)


    // POST requenst for suggestion words.
    $.post("/suggest", postParameters, function(responseJSON){

        var suggestions = [];
        responseObject = JSON.parse(responseJSON);
        suggestions = responseObject.suggestions;
        console.log(suggestions);

        // fill the dropdown box.
        for (var i = 1; i < 6; i++) {
            var istring = "#" + i.toString();
            if (typeof suggestions[i - 1] === 'undefined') {
                $(istring).text("");
            }
            $(istring).text(suggestions[i - 1])
        };

    });

});