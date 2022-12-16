function handleTopAndBottomPressure() {
    var $session = ctx().session;
    var $parseTree = ctx().parseTree;
    
    if (!$session.topPulse) {
        $session.topPulse = $parseTree.value; 
        sendTextResponse($session.topPulse);
        sendTextResponse("А теперь назовите нижнее артериальное давление");
        $reactions.transition({value: "/newNode_1", deferred: true});
        
        return;
    }
    
    if (!$session.bottomPulse) {
        $session.bottomPulse = $parseTree.value;
        sendTextResponse($session.bottomPulse);
        $reactions.transition({value: "/newNode_2", deferred: false});
        
        return;
    }
}

function handleFullPressure() {
    var $parseTree = $jsapi.context().parseTree;
    
    sendTextResponse($parseTree.Number[0].value + '/' + $parseTree.Number[1].value);
    $reactions.transition('/newNode_2');
}

function getName() {
    return $dialer.getPayload().name;
}