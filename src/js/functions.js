function handleTopAndBottomPressure() {
    var $session = ctx().session;
    var $parseTree = ctx().parseTree;
    
    if (!$session.topDavl) {
        $session.topDavl = $parseTree.value; 
        sendTextResponse("А теперь назовите нижнее артериальное давление");
        $reactions.transition({value: "/newNode_1", deferred: true});
        
        return;
    }
    
    if (!$session.bottomDavl) {
        $session.bottomDavl = $parseTree.value;
        $session.artDavl = $session.topDavl + '/' + $session.bottomDavl;
        $reactions.transition({value: "/newNode_2", deferred: false});

        return;
    }
}

function handleFullPressure() {
    var $parseTree = $jsapi.context().parseTree;
    
    $reactions.transition('/newNode_2');
}

function getName() {
    return $dialer.getPayload().name;
}

function sendTextResponse(text) {
    var response = ctx().response;
    
    response.replies = response.replies || [];
    
    response.replies.push({
        type: 'text',
        text: text,
    });
}

function ctx() {
    return $jsapi.context();
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
    var $session = ctx().session;
    
    var bodyData = {
        "ff94ed3a-7ce8-4952-b731-0435d58e6110": $session.artDavl,
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
    
    var response = $http.post("https://api.dev.doctis.app/api/remote-monitoring/calling_result", {
        dataType: 'json',
        body: bodyData,
    });
    
    if (response.isOk) {
        $reactions.transition("/newNode_31");
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
    var $session = ctx().session;
    
    var bodyData = {
        ownerType: "0",
        callId: getCallId(),
    }
    
    var response = $http.post("https://api.dev.doctis.app/api/remote-monitoring/change_owner_type", {
        dataType: 'json',
        body: bodyData,
    });
    
    $reactions.transition("/newNode_32");
    
    echo(response);
}