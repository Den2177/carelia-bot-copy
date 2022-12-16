theme: /
    state: newNode_0
        a: {{getName()}}, добрый день! Вас приветствует центр дистанционного контроля состояния здоровья пациентов. || tts = "{{getName()}}, добрый день! Вас приветствует центр дистанционного контроля состояния здоровья пациентов.", ttsEnabled = true
        a: Подскажите, удобно  вам сейчас ответить на ряд вопросов касательно  вашего состояния? || tts = "Подскажите, удобно  вам сейчас ответить на ряд вопросов касательно  вашего состояния?", ttsEnabled = true
        go!: /newNode_23
        
    state: newNode_23
        state: 1
            q: $AGREEMENT
            q: да*
            q: удобно*

            go!: /newNode_1

        state: 2
            q: $NEGATION
            q: нет*
            q: неа*

            go!: /

    state: newNode_1
        a: Назовите свое артериальное давление
        
        state: fullPressure
            q: * $Number *
            script: 
                handleFullPressure();
                
        state: onePartPressure
            script:
                handleTopAndBottomPressure();
            
        # InputNumber:
        #     actions = [{"buttons":[],"type":"buttons"}]
        #     prompt = Назовите самую большую цифру артериального давления?
        #     varName = artDavl
        #     failureMessage = ["Назовите самую большую цифру артериального давления?"]
        #     then = /newNode_2
        #     minValue = 1
        #     maxValue = 220
        #     html = Назовите самую большую цифру артериального давления?
        #     htmlEnabled = false
        #     failureMessageHtml = ["Назовите самую большую цифру артериального давления?"]
    
    state: 23
        # q!: $regex</start>
            # a: Назовите свой полис ?
        state: newNode_022
            
                
            q: $regex<([0-9]{3}|[0-9]{2})[/]{1}([0-9]{3}|[0-9]{2})>
                
            script:
                $session.polis1 =  String($parseTree._Number);
                var numbers = _.pluck($parseTree.Number1, "value")
                numbers = String(numbers).replace(/,/g, '')
                # $reactions.answer("полис: " + numbers)
                $session.polis = numbers
            go!: /prov 
            
    state: prov
        a: {{$session.polis}}
        go!: /23

    state: newNode_2
        InputNumber:
            actions = [{"buttons":[],"type":"buttons"}]
            prompt = Скажите, пожалуйста, максимальный пульс, измеренный в покое ?
            varName = puls
            failureMessage = ["Назовите цифру вашего пульса от 70 до 220"]
            then = /newNode_3
            minValue = 70
            maxValue = 250
            html = Скажите, пожалуйста, максимальный пульс, измеренный в покое ?
            htmlEnabled = false
            failureMessageHtml = ["Назовите цифру вашего пульса от 70 до 220"]

    state: newNode_3
        a: Беспокоят ли Вас давящие, сжимающие  боли в области сердца, в течение недели? || tts = "Беспокоят ли Вас давящие, сжимающие  боли в области сердца, в течение недели?", ttsEnabled = true
        go!: /newNode_4

    state: newNode_4
        state: 1
            q: $AGREEMENT
            q: Иногда да*
            q: Да*
            q: Иногда
            q: Переодически
            q: *Беспокоит*

            go!: /newNode_246

        state: 2
            q: $NEGATION
            q: нет*
            q: не*
            q: Не беспокоит
            q: Небеспокоит
            q: *Не беспокоит*

            go!: /newNode_24

        state: Fallback
            event: noMatch
            go!: /newNode_86
    state: newNode_26
        InputText:
            actions = [{"buttons":[],"type":"buttons"}]
            prompt = 
            varName = angBol
            html = 
            htmlEnabled = false
            then = /newNode_6

    state: newNode_24
        script:
            $session.angBol = "Нет";
            $session.pristup = 0;
            $session.nitrat = 0;
        # Transition /newNode_25
        go!: /newNode_6

    state: newNode_246
        script:
            $session.angBol = "Да";
        # Transition /newNode_25
        go!: /newNode_5

    state: newNode_5
        InputNumber:
            actions = [{"buttons":[],"type":"buttons"}]
            prompt = Сообщите, пожалуйста, количество приступов сжимающей боли?
            varName = pristup
            failureMessage = ["Назовите количество приступов от 0 до 20"]
            then = /newNode_7
            minValue = 0
            maxValue = 20
            html = Сообщите, пожалуйста, количество приступов сжимающей боли?
            htmlEnabled = false
            failureMessageHtml = ["Назовите количество приступов от 0 до 20"]

    state: newNode_7
        InputNumber:
            actions = [{"buttons":[],"type":"buttons"}]
            prompt = Cколько приступов сжимающей боли вы купировали нитратами?
            varName = nitrat
            failureMessage = ["Назовите цифру, от 0 до 20 cколько приступов сжимающей боли вы купировали нитратами?"]
            then = /newNode_6
            minValue = 0
            maxValue = 20
            html = Cколько приступов сжимающей боли вы купировали нитратами?
            htmlEnabled = false
            failureMessageHtml = ["назовите цифру, от 0 до 20 сколько приступов сжимающей боли вы купировали нитратами?"]

    state: newNode_6
        a: Отмечаете ли Вы появление или нарастание одышки? || tts = "Отмечаете ли Вы появление или нарастание одышки?", ttsEnabled = true
        go!: /newNode_8
        
    state: newNode_8
        state: 1
            q: $AGREEMENT
            q: Иногда да*
            q: Да*
            q: Иногда
            q: Переодически
            q: Отмечаю*
            q: *Отмечаю*

            go!: /newNode_11

        state: 2
            q: $NEGATION
            q: нет*
            q: не*
            q: Не отмечаю*
            q: Неотмечаю*
            q: *нету*

            go!: /newNode_12

        state: Fallback
            event: noMatch
            go!: /newNode_86

    state: newNode_11
        script:
            $session.dyspnea = "Да";
        # Transition /newNode_13
        go!: /newNode_9

    state: newNode_12
        script:
            $session.dyspnea = "Нет";
        # Transition /newNode_14
        go!: /newNode_9

    state: newNode_15
        InputText:
            actions = [{"buttons":[],"type":"buttons"}]
            prompt = 
            varName = dypnesia
            html = 
            htmlEnabled = false
            then = /newNode_9

    state: newNode_9
        a: Есть ли у Вас отеки на ногах? || tts = "Есть ли у Вас отеки на ногах?", ttsEnabled = true
        go!: /newNode_10
    state: newNode_10
        state: 1
            q: $AGREEMENT
            e: $yes
            q: да*
            q: $AGREEMENT
            q: Иногда да*
            q: Да*
            q: Иногда
            q: Переодически

            go!: /newNode_16

        state: 2
            q: $NEGATION
            q: нет*
            q: не*
            q: Нету*
            q: *Не, нету*

            go!: /newNode_18

        state: 3
            q: Да , только по утрам*
            q: по утрам*
            q: утром*
            q: утро*
            q: утречком*

            go!: /newNode_19

        state: Fallback
            event: noMatch
            go!: /newNode_86

    state: newNode_16
        script:
            $session.oteki = "Да";
        # Transition /newNode_17
        go!: /newNode_21

    state: newNode_18
        script:
            $session.oteki = "Нет";
        # Transition /newNode_29
        go!: /newNode_21

    state: newNode_19
        script:
            $session.oteki = "Да, только по утрам";
        # Transition /newNode_30
        go!: /newNode_21

    state: newNode_22
        InputText:
            actions = [{"buttons":[],"type":"buttons"}]
            prompt = 
            varName = oteki
            html = 
            htmlEnabled = false
            then = /newNode_21

    state: newNode_27
        script:
            $session.oteki = 0;
        # Transition /newNode_28
        go!: /newNode_21

    state: newNode_21
        a: Спасибо за Ваши ответы, информация будет передана вашему лечащему врачу, всего вам доброго! || tts = "Спасибо за Ваши ответы, информация будет передана вашему лечащему врачу, всего вам доброго!", ttsEnabled = true
        go!: /zapros
    
   
    state: newNode_99
        TransferCallToOperator:
            phoneNumber = 778
            then = /newNode_52    

    state: zapros
        HttpRequest:
            url = https://api.dev.doctis.app/api/remote-monitoring/calling_result
            method = POST
            dataType = 
            body = {"name": "{{getName()}}", "ff94ed3a-7ce8-4952-b731-0435d58e6110": "{{$session.artDavl}}","285ba85f-eb0e-4401-b5b5-28fa332ad74e": "{{$session.puls}}","bf860574-61ee-4ddb-b4f3-ec2e04f62e33":"{{$session.angBol}}", "0e262c5a-cf95-4239-890e-b2a0ec73d09d": "{{$session.pristup}}","3b78b736-8d2a-4b52-b716-7b770e8fb29a": "{{$session.nitrat}}","440fbd2c-279c-4f59-96f4-c65095adaedd": "{{$session.dyspnea}}","2f0ddee1-2056-4496-9c44-68815da4cade": "{{$session.oteki}}","patient_id": "{{$session.rawRequest.originateData.payload.patient_id}}", "call_id": "{{$session.rawRequest.originateData.payload.call_id}}"}
            # headers = [{"ApiKey":"a9db7c01-e309-4a61-b04d-faffdfd020c0"}]
            okState = /newNode_31
            errorState = 
            timeout = 0
            vars =  

    state: newNode_31
        EndSession:
    
    state: zapros1
        HttpRequest:
            url = https://api.dev.doctis.app/api/remote-monitoring/change_owner_type
            method = POST
            dataType = 
            # headers = [{"ApiKey": "a9db7c01-e309-4a61-b04d-faffdfd020c0"}]
            body = {"callId":"{{$session.rawRequest.originateData.payload.call_id}}","ownerType": "{{0}}"}
            okState = /newNode_32
            errorState = /newNode_32
            timeout = 0
            vars = 
    
    state: newNode_32
        TransferCallToOperator:
            phoneNumber = 778 
            then = 
            errorState = 
    
    state: newNode_86
        a: Не могу распознать вашу речь , сейчас будет произведен перевод на оператора, оставайтесь на линии || tts = "Не могу распознать вашу речь , сейчас будет произведен перевод на оператора, оставайтесь на линии", ttsEnabled = true
        go!: /zapros1
    state: newNode_87
        state: 1
            q: $SWITCH

            go!: /zapros1
    state: newNode_56
        state: 1
            q!: Отрицательный*
            q!: помогите*
            q!: человек*
            q!: умираю*
            

            go!: /zapros1
    state: newNode_149
        state: 1
            q!: Отказ*
            go!: /zapros1
            
    state: redirectToOperator
        q!: $SWITCH
        go!: /newNode_86