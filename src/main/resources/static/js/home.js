var CONTACTS_URL = '/api/contacts';

var EDIT_BUTTON = "<button type='button' onclick='editContact({0})' class='btn btn-info pull-right'>" +
    " <span class='glyphicon glyphicon-pencil'></span> </button>";

var REMOVE_BUTTON = "<button type='button' onclick='removeContact({0});' class='btn btn-warning pull-right'>" +
    " <span class='glyphicon glyphicon-trash'></span> </button>";

// Array contacts //
var contacts;

$(function () {

    loadContacts();

    // On Add or Edit contact //
    $('#contact-form').submit(function (e) {
        e.preventDefault();

        var contact = {
            id: $('#contact-form input[name=id]').val(),
            firstName: $('#contact-form input[name=firstName]').val(),
            middleName: $('#contact-form input[name=middleName]').val(),
            lastName: $('#contact-form input[name=lastName]').val(),
            email: $('#contact-form input[name=email]').val(),
            mobilePhoneNumber: $('#contact-form input[name=mobilePhoneNumber]').val(),
            homePhoneNumber: $('#contact-form input[name=homePhoneNumber]').val(),
            address: $('#contact-form input[name=address]').val(),
        }

        var data = JSON.stringify(contact);

        var httpMethod = contact.id == "" ? "POST" : "PUT";

        $.ajax({
            url: CONTACTS_URL,
            type: httpMethod,
            data: data,
            contentType: "application/json",
            statusCode: {
                201: function () {
                    $("#contact-form-container").hide();
                    loadContacts();
                },
                202: function () {
                    $("#contact-form-container").hide();
                    loadContacts();
                },
                422: renderErrors,
                400: onLoginPage,
                302: onLoginPage,
            }
        });

    });

    // Search //
    $('#search').keyup(function () {
        var value = $('#search').val();

        if (value.length > 0) {
            loadContacts(value)
        } else {
            loadContacts();
        }
    });

    // Close Form //
    $('#add-cancel').click(function () {
        $("#contact-form-container").hide();
        cleanForm();
    });

    // Open Form //
    $("#add-contact-button").click(function () {
        cleanForm();
        $("#contact-form-container").show();
    });

    // Logout //
    $("#logout").click(function () {
        $.post("/logout");
        document.location.href = "/";
    });

});

// Clean form //
function cleanForm() {
    $('#contact-form input[name=id]')[0].value = "";
    $('#contact-form input[name=firstName]')[0].value = "";
    $('#contact-form input[name=middleName]')[0].value = "";
    $('#contact-form input[name=lastName]')[0].value = "";
    $('#contact-form input[name=email]')[0].value = "";
    $('#contact-form input[name=mobilePhoneNumber]')[0].value = "";
    $('#contact-form input[name=homePhoneNumber]')[0].value = "";
    $('#contact-form input[name=address]')[0].value = "";

    removeErrorMessages();
}

// Edit contact //
function editContact(id) {
    var c = contacts[id];

    $('#contact-form input[name=id]')[0].value = c.id;
    $('#contact-form input[name=firstName]')[0].value = c.firstName;
    $('#contact-form input[name=middleName]')[0].value = c.middleName;
    $('#contact-form input[name=lastName]')[0].value = c.lastName;
    $('#contact-form input[name=email]')[0].value = c.email;
    $('#contact-form input[name=mobilePhoneNumber]')[0].value = c.mobilePhoneNumber;
    $('#contact-form input[name=homePhoneNumber]')[0].value = c.homePhoneNumber;
    $('#contact-form input[name=address]')[0].value = c.address;

    removeErrorMessages();

    $("#contact-form-container").show();
}

// Remove contact by ID //
function removeContact(id) {

    $.ajax({
        url: CONTACTS_URL + "/" + id,
        type: 'DELETE',
        success: function () {
            loadContacts();
        }
    });
}

// Load and display all contacts //
function loadContacts(pattern) {
    var url = "";

    if (pattern != undefined) {
        url = CONTACTS_URL + "/find?pattern=" + pattern;
    } else {
        url = CONTACTS_URL;
    }

    $.getJSON(url, function (data) {
        var table = document.getElementById("contact-table");

        // Reset index and table//
        contacts = new Object();
        while (table.rows.length > 1) {
            table.deleteRow(1);
        }

        // Draw and index data //
        data.forEach(function (contact, i, arr) {
            var row = table.insertRow();
            row.id = contact.id;
            row.insertCell(0).innerText = contact.lastName;
            row.insertCell(1).innerText = contact.firstName;
            row.insertCell(2).innerText = contact.middleName;
            row.insertCell(3).innerText = contact.mobilePhoneNumber;
            row.insertCell(4).innerText = contact.homePhoneNumber;
            row.insertCell(5).innerText = contact.email;
            row.insertCell(6).innerText = contact.address;
            row.insertCell(7).innerHTML = EDIT_BUTTON.format(contact.id);
            row.insertCell(8).innerHTML = REMOVE_BUTTON.format(contact.id);

            contacts[contact.id] = contact; // Used for getting contact by ID //
        })
    });
}

function onLoginPage() {
    document.location.href = "/login";
}

// Sting util method //
if (!String.prototype.format) {
    String.prototype.format = function () {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] != 'undefined'
                ? args[number]
                : match
                ;
        });
    };
}