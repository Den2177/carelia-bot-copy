function с() {
    return $jsapi.context();
}

function handleTopAndBottomPressure() {
    var $session = с().session;
    var $parseTree = с().parseTree;
    
    if (!$session.topDavl) {
        
        var topDavl = $parseTree.value;
        
        if (!validatePressure(topDavl, true)) {
            sendTextResponse("Пожалуйста, назовите давление от сорока до трехсот");
            return;
        }
        
        $session.topDavl = $parseTree.value; 
        sendTextResponse("Вы назвали только верхнее артериальное давление. Осталось назвать нижнее");
        $reactions.transition({value: "/newNode_1", deferred: true});
        
        return;
    }
    
    if (!$session.bottomDavl) {
        var bottomDavl = $parseTree.value;
        
        if (!validatePressure(bottomDavl, false)) {
            sendTextResponse("Пожалуйста, назовите давление от шестидесяти до ста восьмидесяти");
            return;
        }
        
        $session.bottomDavl = bottomDavl;
        $reactions.transition({value: "/newNode_2", deferred: false});
        
        return;
    }
}

function handleFullPressure() {
    var $parseTree = c().parseTree;
    var $session = c().session;
    
    var topDavl = $parseTree.Number[0].value;
    var bottomDavl = $parseTree.Number[1].value;
    
    if (!validatePressure(topDavl, true) || !validatePressure(bottomDavl, false)) {
        sendTextResponse("Пожалуйста, назовите давление от сорока на шестьдесят до триста на сто восьмидесят");
        return;
    }

    $session.topDavl = topDavl;
    $session.bottomDavl = bottomDavl;
    
    $reactions.transition('/newNode_2');
}

function getName() {
    return $dialer.getPayload().name;
}

function sendTextResponse(text) {
    var response = с().response;
    
    response.replies = response.replies || [];
    
    response.replies.push({
        type: 'text',
        text: text,
    });
}


// HttpRequest:
//             url = https://api.dev.doctis.app/api/remote-monitoring/calling_result
//             method = POST
//             dataType = 
//             body = {"name": "{{getName()}}", "ff94ed3a-7ce8-4952-b731-0435d58e6110": "{{$session.artDavl}}","285ba85f-eb0e-4401-b5b5-28fa332ad74e": "{{$session.puls}}","bf860574-61ee-4ddb-b4f3-ec2e04f62e33":"{{$session.angBol}}", "0e262c5a-cf95-4239-890e-b2a0ec73d09d": "{{$session.pristup}}","3b78b736-8d2a-4b52-b716-7b770e8fb29a": "{{$session.nitrat}}","440fbd2c-279c-4f59-96f4-c65095adaedd": "{{$session.dyspnea}}","2f0ddee1-2056-4496-9c44-68815da4cade": "{{$session.oteki}}","patient_id": "{{$session.rawRequest.originateData.payload.patient_id}}", "call_id": "{{$session.rawRequest.originateData.payload.call_id}}"}
//             # headers = [{"ApiKey":"a9db7c01-e309-4a61-b04d-faffdfd020c0"}]
//             okState = /newNode_31
//             errorState = 
//             timeout = 0
//             vars =  

function sendData() {
    var $session = c().session;
    
    var bodyData = {
        "a34f2480-0e2d-49b8-af88-7724890aff5b": $session.topDavl,
        "e78c3c0b-6996-4130-bc3d-be6ace32ff53": $session.bottomDavl,
        "285ba85f-eb0e-4401-b5b5-28fa332ad74e": $session.puls,
        "bf860574-61ee-4ddb-b4f3-ec2e04f62e33": $session.angBol,
        "0e262c5a-cf95-4239-890e-b2a0ec73d09d": $session.pristup,
        "3b78b736-8d2a-4b52-b716-7b770e8fb29a": $session.nitrat,
        "440fbd2c-279c-4f59-96f4-c65095adaedd": $session.dyspnea,
        "2f0ddee1-2056-4496-9c44-68815da4cade": $session.oteki,
        name: getName(),
        patient_id: getPatientId(),
        call_id: getCallId(),
    }
    
    echo(bodyData);
    
    var response = $http.post("https://api.dev.doctis.app/api/remote-monitoring/calling_result", {
        dataType: 'json',
        body: bodyData,
    });
    
    if (response.isOk) {
        $reactions.transition("/newNode_31");
    } else {
        $reactions.transition("Произошла ошибка, попробуйте пройти тест заного");
    }
    
    echo(response);
}

function getPatientId() {
    return $dialer.getPayload().patient_id;
}

function getCallId() {
    return $dialer.getPayload().call_id;
}

function echo(data) {
    log(toPrettyString(data));
}

/*HttpRequest:
            url = https://api.dev.doctis.app/api/remote-monitoring/change_owner_type
            method = POST
            dataType = 
            # headers = [{"ApiKey": "a9db7c01-e309-4a61-b04d-faffdfd020c0"}]
            body = {"callId":"{{$session.rawRequest.originateData.payload.call_id}}","ownerType": "{{0}}"}
            okState = /newNode_32
            errorState = /newNode_32
            timeout = 0
            vars = */
            
function changeOwnerType() {
    var $session = c().session;
    
    var bodyData = {
        ownerType: "0",
        callId: getCallId(),
    }
    
    var response = $http.post("https://api.dev.doctis.app/api/remote-monitoring/change_owner_type", {
        dataType: 'json',
        body: bodyData,
    });
    
    if (response.isOk) {
        $reactions.transition("/newNode_32");
    } else {
        sendTextResponse("Произошла ошибка, перезвоните попозже");
    }
    
    echo(response);
}

function validatePressure(value, isTop) {
    var $session = c().session;

    if (isTop) {
        if (value < 40 || value > 300) {
            
            $session.topDavl = null;
            $reactions.transition('/newNode_1');
            
            return false;
        }
        
        return true;
        
    } else if (!isTop) {
         if (value < 60 || value > 180) {
            
            $session.bottomDavl = null;
            $reactions.transition('/newNode_1');
            
            return false;
        }
        
        return true;
    }
}


function handleNumbers(min, max, nextNode, field, failureMessage) {
    var number = +c().parseTree._Number;
    var $session = c().session;
    
    if (number < min || nubmer > max) {
        sendResponse(failureMessage);
        return;
    }
    
    $session[field] = number;
    $reactions.transition(nextNode);
}