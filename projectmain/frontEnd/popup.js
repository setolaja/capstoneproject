$(document).ready(function(){
  $('#myButton').keydown(function() {
    var key = e.which;
    if (key == 13) {
    // As ASCII code for ENTER key is "13"
    $('#myButton').submit(); // Submit form code
    }
  });
  $("#myButton").click(function(){
    //var bla = $('#input_text').val();
    // alert(bla);
    //alert("click works");
    $.post("http://localhost:4567/query",
    {
      input_text: $('#input_text').val()
    },
    function(data,status){
      //call new pop up html
      //alert(data);
      var retString = cannedResponse(data);
      var response = new SpeechSynthesisUtterance(retString);
      window.speechSynthesis.speak(response);
      alert(retString);
      
      if(data.response!=null && status=="Success"){
        var start_div = document.getElementById('start');
        //make a new Div element
        var newElement = document.createElement('div');
        //add text to that div
        newElement.innerHTML = data.repsonse;
        //append it to the main 
        start_div.appendChild(newElement);
      }
      //alert('Hello, World!'); 
      

    });
  });
  
});
var cannedResponse = function(oldquery){
    var query = JSON.parse(oldquery);
    
    var response = "";
    if(query == null){
        response += "I'm sorry, im not sure how to answer that.";
        return response;
    }
    switch (query.type.toString()){

        /**
         * If the query requested is for weather
         */
        case "weather": {
            var resp =  query.response;
            response = "Right now in " + resp.location + ", it is " + resp.temp + " degrees and there are " + resp.weather + ".";
            break;
        }

        /**
         * If the query requested is for flip a coin
         */
        case "coin": {
            response = query.response + ".";
            break;
        }

        /**
         * If the query requested is for calculator
         */
        case "calc": {
            response = query.quer + "is" + query.response.toString() + ".";
            break;
        }

        /**
         * If the query requested is for startup
         */
        case "startup": {
            response = query.get("response") + ".";
            break;
        }

        /**
         * If the query requested is for what time is it
         */
        case "time": {
            response = "The time is " + query.response + ".";
            break;
        }

        /**
         * If the query requested is for brief me the news
         */
        case "news": {
            var responseArray = query.response;
            response = responseArray[1] + ".";
            break;
        }

        /**
         * If the query requested is for random number
         */
        case "random": {
            response = query.response.toString() + ".";
            break;
        }

        /**
         * If the query requested is for a professors office hours
         */
        // case "officehours": {
        //     Time startT;
        //     Time endT;
        //     String dayOfWeek;
        //     ArrayList<Database.RowData> aList = (ArrayList<Database.RowData>) query.get("response");
        //     /**
        //      * Technical debt: building name and room number take from the first row only, so if they change it would let the user know
        //      */

        //     response += "Office hours for " + aList.get(0).professorName + " are in " + aList.get(0).buildingName +
        //     " in room number " + aList.get(0).roomNumber;
        //     for(int i = 0; i < aList.size(); i++){
        //         startT = aList.get(i).startT;
        //         endT = aList.get(i).endT;
        //         dayOfWeek = aList.get(i).dayOfWeek;
        //         //Add data to response
        //         if(i == aList.size() -1){
        //             response += " on " + dayOfWeek + "'s from " + startT.toString().substring(0,5) + " to " + endT.toString().substring(0,5) + ".";
        //         }
        //         else {
        //             response += " on " + dayOfWeek + "'s from " + startT.toString().substring(0,5) + " to " + endT.toString().substring(0,5) + ", and";
        //         }
        //     }

        //     return response;
        // }

        // case "buildinghours": {
        //     ArrayList<Database.BuildingData> aList = (ArrayList<Database.BuildingData>) query.get("response");
        //     response += "The hours for " + aList.get(0).buildingName + " are from " + aList.get(0).startT.toString().substring(0,5) + " to " + aList.get(0).endT.toString().substring(0,5);
        //     response += ".";
        //     return response;
        // }

        /**
         * If the query requested hasnt been implemented
         */
        default: {
            response = "I'm sorry, im not sure how to answer that.";
            break;
        }

    }
    return response;

}
