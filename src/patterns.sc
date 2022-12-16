# require: ../common.js
# require: numberConverter.js
# require: ../number/number.sc
# require: ../common.js
# require: phoneNumber.js
require: main.js

patterns:
    
    
    
    $oneToNineNormForm1 =    (один :1 |
                            два :2 |
                            три :3 |
                            четыре :4 |
                            пять :5 |
                            шесть :6 |
                            семь :7 |
                            восемь :8 |
                            девять :9  ) || converter = $converters.numberConverterValue

    $oneToNineGent1 =    (нуля :0 |
                        (однерки/единицы) :1 |
                        двойки :2 |
                        тройки :3 |
                        четверки :4 |
                        пятерки :5 |
                        шестерки :6 |
                        семерки :7 |
                        восьмерки :8 |
                        девятки :9) || converter = $converters.numberConverterValue
                        
    $dabll = (две двойки :22)                    

    $digitsNormalForm1 = (ноль :0 | $oneToNineNormForm1) || converter = function(pt) { return pt.oneToNineNormForm1 ? pt.oneToNineNormForm1[0].value : 0}

    $twoSameDigits1 = ((две/два) $oneToNineGent1) : 2   || converter = $converters.PhoneNumberSameDigits
    $threeSameDigits1 = (три $oneToNineGent1) : 3  || converter = $converters.PhoneNumberSameDigits
    $fourSameDigits1 = (четыре $oneToNineGent1) : 4  || converter = $converters.PhoneNumberSameDigits

    $dozenAndDigitPN1 = $NumberDozen1 $digitsNormalForm1 || converter = function(parseTree) { return parseTree.NumberDozen1[0].value + parseTree.digitsNormalForm1[0].value }
    $twoDigitNumberPN1 = ($NumberTwoDigit1 / $NumberDozen1 / $dozenAndDigitPN1)

    $hundredAndDozenPN1 = $NumberHundred1 $twoDigitNumberPN1 || converter = function(parseTree) { return parseTree.NumberHundred1[0].value + parseTree.twoDigitNumberPN1[0].value }
    $hundredAndDigitPN1 = $NumberHundred1 $oneToNineNormForm1 || converter = function(parseTree) { return parseTree.NumberHundred1[0].value + parseTree.oneToNineNormForm1[0].value }
    $threeDigitNumberPN1 = ($NumberHundred1 / $hundredAndDozenPN1 / $hundredAndDigitPN1)

    $NumberThousandsWithDigitPN1 = $NumberOneDigit1 (тысяч*|тыщ*|thousand*) || converter = function(parseTree) { return (1000 * parseTree.NumberOneDigit1[0].value) }
    $NumberThousandsWithOutDigitPN1 = [одна] (тысяча|тыща|thousand*):1000
    $NumberThousandsPN1 = ($NumberThousandsWithDigitPN1 / $NumberThousandsWithOutDigitPN1)

    $ThousandsAndDigitPN1 = $NumberThousandsPN1 $digitsNormalForm1 || converter = function(parseTree) { return parseInt(parseTree.NumberThousandsPN1[0].value) + parseTree.digitsNormalForm1[0].value }
    $ThousandsAndHundredPN1 = $NumberThousandsPN1 $threeDigitNumberPN1 || converter = function(parseTree) { return parseInt(parseTree.NumberThousandsPN1[0].value) + parseInt(parseTree.threeDigitNumberPN1[0].value) }
    $fourDigitNumberPN1 = ($NumberThousandsPN1 / $ThousandsAndHundredPN1 / $ThousandsAndDigitPN1 )

    $ThreeDigitsPN1 = (
                $threeSameDigits1 /
                $zeroPN1 $twoDigitNumberPN1 /
                $twoSameDigits1 $digitsNormalForm1 /
                $digitsNormalForm1 $digitsNormalForm1 $digitsNormalForm1 /
                $threeDigitNumberPN1) || converter = $converters.collectParseTreeValues

    $FourDigitsPN1 = (
                $digitsNormalForm1 $digitsNormalForm1 $digitsNormalForm1 $digitsNormalForm1 / 
                $zeroPN1 $digitsNormalForm1 $twoDigitNumberPN1 / 
                $zeroPN1 $threeDigitNumberPN1 / 
                $twoDigitNumberPN1 $twoDigitNumberPN1 / 
                $twoDigitNumberPN1 $zeroPN1 $digitsNormalForm1 / 
                $twoSameDigits1 $zeroPN1 $digitsNormalForm1 / 
                $twoSameDigits1 $twoDigitNumberPN1 / 
                $threeSameDigits1 $digitsNormalForm1 /
                $fourSameDigits1 /
                $fourDigitNumberPN1) || converter = $converters.collectParseTreeValues



    $patternNine1 = (~девять|~девятка|девят*|девятый|~девяточка|nine|ninth):9
    $patternNinety1 = (девяносто|девяност*|ninety|ninetieth):90 
    $patternNinetyAndDigit1 = $patternNinety1 $digitsNormalForm1 || converter = function(parseTree) { return parseTree.patternNinety1[0].value + parseTree.digitsNormalForm1[0].value }

    $patternNineHundred1 = (девятьсот|девятисот*|девятиста/~девять ~сотня):900 
    $NineHundredAndDozenPN1 = $patternNineHundred1 $twoDigitNumberPN1 || converter = function(parseTree) { return parseInt(parseTree.patternNineHundred1.value) + parseInt(parseTree.twoDigitNumberPN1[0].value) }
    $NineHundredAndDigitPN1 = $patternNineHundred1 $oneToNineNormForm1 || converter = function(parseTree) { return parseInt(parseTree.patternNineHundred1[0].value) + parseInt(parseTree.oneToNineNormForm1[0].value) }

    $threeDigitNumberNinePN1 = ($patternNineHundred1 / $NineHundredAndDozenPN1 / $NineHundredAndDigitPN1)

    $ThreeDigitsStartsWith91 = ($patternNine $twoSameDigits1 / $patternNine1 $digitsNormalForm1 $digitsNormalForm1 / $patternNine1 $twoDigitNumberPN1 / ($patternNinety1/$patternNinetyAndDigit1) $digitsNormalForm1 / $threeDigitNumberNinePN1) || converter = $converters.collectParseTreeValues


    $eightOrSevenPN1 = ([плюс/+] (~семь|седьм*|семи|семер*|seven|seventh):7 | (~восемь|восьм*|восем|eight*):8 )


    $mobilePhoneNumberInWords1 = [$eightOrSevenPN1] $ThreeDigitsStartsWith91 $ThreeDigitsPN1 $FourDigitsPN1 || converter = $converters.mobilePhoneNumberInWordsConverter

    $mobilePhoneNumberInDigits1 = ($regexp<(\+?(8|7)[\-\s]?)?\(?9\d{2}\)?[\-\s]?\d{3}[\-\s]?\d{2}[\-\s]?\d{2}>) || converter = $converters.mobilePhoneNumberInDigitsConverter
    

    $mobilePhoneNumber1 = ($mobilePhoneNumberInDigits1 / $mobilePhoneNumberInWords1)
    
    $NumberDigit1 = $regexp<\d+>
            || converter = $converters.numberConverterDigit
    
    $NubmerCommaSeparated1 = $regexp<\d{1,3}((\.|\s)\d{3})+>
            || converter = $converters.numberConverterCommaSeparatedDigit

    $NumberOrdinalDigit1 = $regexp<\d?(1st|2nd|3rd|\dth)>
            || converter = $converters.numberConverterOrdinalDigit

    $NumberOneDigit1 = (
        (~ноль|~нулевой|~нулевого|нол*|нул*|zero):0 | 
        (~один|~одна|перв*|~одного|единиц*|единичк*|однерка|однёрка|one|first):1 | 
        (~два|~второй|~второго|две|дву*|втор*|пара|пару|двое|~двойка|~двоечка|two|[a] couple of|second):2 | 
        (~три|~третий|~третьего|трех|трое|three|~тройка|~троечка|~трешечка|third):3 |
        (~четыре|~четвертый|~четвертого|четырех|четверо|~четверка|~четверочка|*four|fourth):4 |
        (~пять|~пятый|~пятого|пята*|пято*|пятый|пятым*|пяти|пятер*|five|fifth):5 | 
        (~шесть|~шестой|~шестого|шест*|шестым*|шести|шестер*|six|sixth):6 | 
        (~семь|~седьмой|~седьмого|седьм*|семи|семер*|seven|seventh):7 | 
        (~восемь|~восьмой|~восьмого|восьм*|восем|eight*):8 | 
        (~девять|~девятый|~девятого|~девятка|девят*|девятый|~девяточка|nine|ninth):9 )
            || converter = $converters.numberConverterValue

    $NumberTwoDigit1 = (
        (~десять|десятый|десятое|десят*|ten|tenth):10 | 
        (~одиннадцать|одиннадцатый|одиннадцатое|одиннадцат*|eleven*):11 | 
        (~двенадцать|двенадцатый|двенадцатое|двенадцат*|twelve|twelfth):12 | 
        (~тринадцать|тринадцатый|тринадцатое|тринадцат*|thirteen*):13 | 
        (~четырнадцать|четырнадцатый|четырнадцатое|четырнадцат*|fourteen*):14 | 
        (~пятнадцать|пятнадцатый|пятнадцатое|пятнадцат*|fifteen*):15 | 
        (~шестнадцать|шестнадцатый|шестрадцатое|шестнадцат*|sixteen*):16 | 
        (~семнадцать|семнадцатый|семнадцатое|семнадцат*|seventeen*):17 | 
        (~восемнадцать|восемнадцатый|восемнадцатое|восемнадцат*|eighteen*):18 | 
        (~девятнадцать|девятнадцатый|девятнадцатое|девятнадцат*|nineteen*):19 )
            || converter = $converters.numberConverterValue

    $NumberNumeric1 = ($NumberOneDigit1 | $NumberTwoDigit1)
            || converter = $converters.propagateConverter

    $NumberSimple1 = ($NumberDigit1 | $NumberOrdinalDigit1 | $NumberNumeric1)
            || converter = $converters.numberConverterSum

    $NumberDozenWithDash1 = $regexp<(twenty|thirty|forty|fifty|sixty|seventy|eighty|ninety)-((one|two|three|four|five|six|seven|eight|nine)|(first|second|third|fourth|fifth|sixth|seventh|eighth|nineth))>
            || converter = $converters.numberConverterDozenDash

    $NumberDozen1 = (
        (~двадцать|двадцатый|двадцатое|двадцат*|twenty|twentieth):20 | 
        (~тридцать|тридцатый|тридцатое|тридцат*|thirty|thirtieth):30 | 
        (~сорок|сороковой|сороковое|сорок*|forty|fortieth):40 | 
        (~пятьдесят|пятидесятый|пятидесятое|пятидесят*|fifty*|fiftieth):50 | 
        (~шестьдесят|шестидесятый|шестидесятое|шестидесят*|шисят|sixty|sixtieth):60 | 
        (~семьдесят|семидесятый|семидесятое|семидесят*|seventy|seventieth):70 | 
        (~восемьдесят|восьмидесятый|восьмидесят*|eighty|eightieth):80 | 
        (~девяносто|девяностый|девяностое|девяност*|ninety|ninetieth):90 )
            || converter = $converters.numberConverterValue

    $NumberHundred1 = (
        (~сто|~сотый|сто|ста|[~один] ~сотня|~соточка):100 | 
        (~двести|двухсотый|двухсотое|двести|двухсот*|двухста|~два ~сотня):200 | 
        (~триста|трехсотый|трехсотое|триста|тристо|трехсот*|трёхста|трехста/~три ~сотня):300 | 
        (~четыреста|четырехсотый|четырехсотое|четырест*|четырехсот*|четырехста|четырёста/~четыре ~сотня):400 | 
        (~пятьсот|пятисотый|пятисотое|пятьсот|пятисот*|пятиста/~пять ~сотня):500 | 
        (~шестьсот|шестисотый|шестисотый|шестьсот|шестисот*|шестиста/~шесть ~сотня):600 | 
        (~семьсот|семисотый|семисотое|семьсот|семисот*|семиста/~семь ~сотня):700 | 
        (~восемьсот|восьмисотый|восьмисотое|восемьсот|восьмисот*|восемьста/~восемь ~сотня):800 | 
        (~девятьсот|девятисотый|девятисотое|девятьсот|девятисот*|девятиста/~девять ~сотня):900 )
            || converter = $converters.numberConverterValue

    $NumberHundredComplex1 = 
        [$NumberDozen1] [$NumberSimple1] hundred*: 100
            || converter = $converters.numberConverterMultiply
        
    $NumberThreeDigit1 = (
        $NumberHundredComplex1 [and] [[$NumberDozen1] [$NumberSimple1]|$NumberDozenWithDash1] |
        $NumberHundred1 [and] [[$NumberTwoDigit1]|[$NumberDozen1] [$NumberSimple1]|$NumberDozenWithDash1] |
        $NumberDozen1 [$NumberSimple1] |
        $NumberDozenWithDash1 |
        $NumberSimple1 
        )
            || converter = $converters.numberConverterSum

    $NumberThousands1 = [$NumberThreeDigit1] (~тысяча|тысячный|тысячное|тысяч*|тыщ*|thousand*):1000
            || converter = $converters.numberConverterMultiply

    $NumberMillions1 = [$NumberThreeDigit1] (~миллион|~милионный|миллион*|million*|millon*):1000000
            || converter = $converters.numberConverterMultiply

    $NumberBillions1 = [$NumberThreeDigit1] (~миллиард|миллиардный|миллиардное|миллиард*|лярд*|billion*|billones*):1000000000
            || converter = $converters.numberConverterMultiply

    $Number1 = ( $fgi |
        $NubmerCommaSeparated1 |
        $NumberBillions1 [and|и] [$NumberMillions1] [and|и] [$NumberThousands1] [and|и] [$NumberThreeDigit1] |
        $NumberMillions1 [and|и] [$NumberThousands1] [and|и] [$NumberThreeDigit1] |
        $NumberThousands1 [and|и] [$NumberThreeDigit1] |
        $NumberThreeDigit1 |
        $NumberTwoDigit1 |
        $NumberThreeDigit1 [and|и] $NumberTwoDigit1 [and|и] $mobilePhoneNumberInWords1 [and|и] $mobilePhoneNumberInDigits1 [and|и] $mobilePhoneNumberInDigits1
         
        
        
        ) || converter = $converters.mobilePhoneNumberInWordsConverter
            # || converter = $converters.numberConverterSum

    $dablNumb =  ( два нуля :00 |
                два ноля :00 |
                две единицы :11 |
                две двойки :22 |
                две тройки :33 |
                две четверки :44 |
                две пятерки :55 |
                две шестерки :66 |
                две семерки :77 |
                две восьмерки :88 |
                две девятки :99 |
                две десятки :1010) || converter = $converters.mobilePhoneNumberInWordsConverter
                
    $tripleNumb = ( Три нуля :000 |
                Три ноля :000 |
                три единицы :111 |
                три двойки :222 |
                три тройки :333 |
                три четверки :444 |
                три пятерки :555 |
                три шестерки :666 |
                три семерки :777 |
                три восьмерки :888 |
                три девятки :999 |
                три десятки :101010) || converter = $converters.mobilePhoneNumberInWordsConverter            
    
    $FourNumb = ( четыре нуля :0000 |
                четыре ноля :0000 |
                четыре единицы :1111 |
                четыре двойки :2222 |
                четыре тройки :3333 |
                четыре четверки :4444 |
                четыре пятерки :5555 |
                четыре шестерки :6666 |
                четыре семерки :7777 |
                четыре восьмерки :8888 |
                четыре девятки :9999 |
                четыре десятки :101010) || converter = $converters.mobilePhoneNumberInWordsConverter
    
    $fiveNumb = ( Пять нулей :00000 |
                пять единиц :11111 |
                 пять двоек :22222 |
                 пять троек :33333 |
                 пять четверок :44444 |
                 пять пятерок :55555 |
                 пять шестерок :66666 |
                 пять семерок:77777 |
                 пять восьмерок :88888 |
                 пять девяток :99999 |
                 пять десятков :101010) || converter = $converters.mobilePhoneNumberInWordsConverter 
                 
    $fgi = ( $tripleNumb | $dablNumb | $FourNumb | $fiveNumb )          
     
    $Numeral1 = (один) : 1 || converter = valueToNumberConverter
    
    $zer = ( два нуля :00) ||  converter = $converters.mobilePhoneNumberInWordsConverter
    
    
    $zeroPN1 = (нол*|нул*|zero):0 || converter = $converters.numberConverterValue
    
theme: /
    state: nlp1 || modal = true
        q: $Number1  || fromState = .
