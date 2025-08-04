/* This script will format and display date and time, 12/24hr format */
let clock_12hr = false;

function clock_change_format() {
    clock_12hr = !clock_12hr;
    update_clock()
}

function update_clock() {
    let dt = new Date()
    let date_str = dt.toDateString()
    let hours = dt.getHours()
    let meridian = "";
    if (clock_12hr) {
        meridian = hours >= 12 ? "PM" : "AM";
        hours = hours % 12;
        if (0 === hours) hours = 12;
    }
    let time_str =
        `${hours.toString().padStart(2, 0)}:` +
        `${dt.getMinutes().toString().padStart(2, 0)}:` +
        `${dt.getSeconds().toString().padStart(2, 0)}` +
        ` ${meridian}`
    document.getElementById("date_label").innerHTML = date_str
    document.getElementById("time_label").innerHTML = time_str
}

update_clock()
setInterval(update_clock, 1000)
