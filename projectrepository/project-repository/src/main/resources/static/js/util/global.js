const SUCCESS = "000";
const FAIL = "-1";

function createElement(element, className, text){
    var el = document.createElement(element);
    if(arguments.length >= 2)
        el.className=className;
    if(arguments.length >= 3)
        el.innerText=text;
    return el;
}