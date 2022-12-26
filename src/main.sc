theme: /
    state: newNode_0
        a: {{getName()}} Добрый день! Вас приветствует центр дистанционного контроля состояния здоровья пациентов. || tts = "{{getName()}}, добрый день! Вас приветствует центр дистанционного контроля состояния здоровья пациентов.", ttsEnabled = true
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

            go!: /otkaz_ot_zvonka
            
    state: otkaz_ot_zvonka
        a: Хорошо, мы свяжемся с вами позже. Всего вам доброго.
        script: 
            $dialer.hangUp();
        go!: /newNode_31    

    state: newNode_1
        a: Назовите свое артериальное давление
        
        state: fullPressure
            q: * $Number на $Number *
            q: * $Number и $Number *
            q: * $Number and $Number *
            
            script: 
                handleFullPressure();
                
        state: onePartPressure
            q: * $Number *
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
        a: Скажите, пожалуйста, максимальный пульс, измеренный в покое ?
        
        state: answer
            q: $Number
            q: * $Number *
            script:
                handleNumbers(70, 220, "/newNode_3", "puls", "Назовите цифру вашего пульса от 70 до 220");

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
        a: Сообщите, пожалуйста, количество приступов сжимающей боли?
        
        state: answer
            q: $Number
            q: * $Number *
            script:
                handleNumbers(0, 20, "/newNode_7", "pristup", "Назовите количество приступов от 0 до 20");

    state: newNode_7
        a: Cколько приступов сжимающей боли вы купировали нитратами?
        
        state: answer
            q: $Number
            q: * $Number *
            script: 
                handleNumbers(0, 20, "/newNode_6", "nitrat", "назовите цифру, от 0 до 20 сколько приступов сжимающей боли вы купировали нитратами?");

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
            q: *да* * по * *утрам * *
            q: * утром *
            q: по утрам*
            q: * только утром *
            q: утром*
            q: утро*
            q: утречком*
            q: * да по утрам *

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
        script: 
            $dialer.hangUp();
        go!: /zapros
    
    state: newNode_99
        TransferCallToOperator:
            phoneNumber = 778
            then = /newNode_52    

    state: zapros
        script: 
            
            sendData();
    state: newNode_31
        EndSession:
    
    state: zapros1
        script:
            changeOwnerType();
    
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
        q!: $canthear
        q!: * оператор *
        q!: * переведите на*
        intent!: /Оператор
        go!: /newNode_86