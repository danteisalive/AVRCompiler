    .file  "main.java"
__SREG__ = 0x3f
__SP_H__ = 0x3e
__SP_L__ = 0x3d
__tmp_reg__ = 0
__zero_reg__ = 1
    .global __do_copy_data
    .global __do_clear_bss
    .text
.global main
    .type   main, @function
main:
    push r29
    push r28
    in r28,__SP_L__
    in r29,__SP_H__
/* prologue: function */
    call _Z18MeggyJrSimpleSetupv 
    /* Need to call this so that the meggy library gets set up */


    # Load constant int 3
    ldi    r24,lo8(3)
    ldi    r25,hi8(3)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # Load constant int 10
    ldi    r24,lo8(10)
    ldi    r25,hi8(10)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # Color expression Meggy.Color.GREEN
    ldi    r22,4
    # push one byte expression onto stack
    push   r22

    ### Meggy.setPixel(x,y,color) call
    # load a one byte expression off stack
    pop    r20
    # load a one byte expression off stack
    pop    r22
    # load a one byte expression off stack
    pop    r24
    call   _Z6DrawPxhhh
    call   _Z12DisplaySlatev

    # Load constant int 123
    ldi    r24,lo8(123)
    ldi    r25,hi8(123)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### Meggy.setAuxLEDs(num) call
    # load a two byte expression off stack
    pop    r24
    pop    r25
    call   _Z10SetAuxLEDsh

    # Push Meggy.Tone.As3 onto the stack.
    ldi    r25, hi8(34323)
    ldi    r24, lo8(34323)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 400
    ldi    r24,lo8(400)
    ldi    r25,hi8(400)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### Meggy.toneStart(tone, time_ms) call
    # load a two byte expression off stack
    pop    r22
    pop    r23
    # load a two byte expression off stack
    pop    r24
    pop    r25
    call   _Z10Tone_Startjj

    # Load constant int 557
    ldi    r24,lo8(557)
    ldi    r25,hi8(557)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### Meggy.delay() call
    # load delay parameter
    # load a two byte expression off stack
    pop    r24
    pop    r25
    call   _Z8delay_msj

    #### if statement

    #### short-circuited && operation
    # &&: left operand

    # True/1 expression
    ldi    r22, 1
    # push one byte expression onto stack
    push   r22

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L3
    brne  MJ_L4
    jmp   MJ_L3

MJ_L4:
    # right operand
    # load a one byte expression off stack
    pop    r24

    # False/0 expression
    ldi    r24,0
    # push one byte expression onto stack
    push   r24
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24

MJ_L3:

    # load condition and branch if false
    # load a one byte expression off stack
    pop    r24
    #load zero into reg
    ldi    r25, 0

    #use cp to set SREG
    cp     r24, r25
    #WANT breq MJ_L0
    brne   MJ_L1
    jmp    MJ_L0

    # then label for if
MJ_L1:

    #### if statement

    # Load constant int 34
    ldi    r24,lo8(34)
    ldi    r25,hi8(34)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 54
    ldi    r24,lo8(54)
    ldi    r25,hi8(54)
    # push two byte expression onto stack
    push   r25
    push   r24

    # equality check expression
    # load a two byte expression off stack
    pop    r18
    pop    r19
    # load a two byte expression off stack
    pop    r24
    pop    r25
    cp    r24, r18
    cpc   r25, r19
    breq MJ_L9

    # result is false
MJ_L8:
    ldi     r24, 0
    jmp      MJ_L10

    # result is true
MJ_L9:
    ldi     r24, 1

    # store result of equal expression
MJ_L10:
    # push one byte expression onto stack
    push   r24

    # load condition and branch if false
    # load a one byte expression off stack
    pop    r24
    #load zero into reg
    ldi    r25, 0

    #use cp to set SREG
    cp     r24, r25
    #WANT breq MJ_L5
    brne   MJ_L6
    jmp    MJ_L5

    # then label for if
MJ_L6:
    jmp    MJ_L7

    # else label for if
MJ_L5:

    # done label for if
MJ_L7:
    jmp    MJ_L2

    # else label for if
MJ_L0:

    #### if statement

    # Load constant int 10
    ldi    r24,lo8(10)
    ldi    r25,hi8(10)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # Load constant int 11
    ldi    r24,lo8(11)
    ldi    r25,hi8(11)
    # push two byte expression onto stack
    push   r25
    push   r24

    # less than expression
    # load a two byte expression off stack
    pop    r18
    pop    r19
    # load a one byte expression off stack
    pop    r24
    # promoting a byte to an int
    tst     r24
    brlt     MJ_L17
    ldi    r25, 0
    jmp    MJ_L18
MJ_L17:
    ldi    r25, hi8(-1)
MJ_L18:
    cp    r24, r18
    cpc   r25, r19
    brlt MJ_L15

    # load false
MJ_L14:
    ldi     r24, 0
    jmp      MJ_L16

    # load true
MJ_L15:
    ldi    r24, 1

    # push result of less than
MJ_L16:
    # push one byte expression onto stack
    push   r24

    # load condition and branch if false
    # load a one byte expression off stack
    pop    r24
    #load zero into reg
    ldi    r25, 0

    #use cp to set SREG
    cp     r24, r25
    #WANT breq MJ_L11
    brne   MJ_L12
    jmp    MJ_L11

    # then label for if
MJ_L12:

    #### while statement
MJ_L19:

    # True/1 expression
    ldi    r22, 1
    # push one byte expression onto stack
    push   r22

    # if not(condition)
    # load a one byte expression off stack
    pop    r24
    ldi    r25,0
    cp     r24, r25
    # WANT breq MJ_L21
    brne   MJ_L20
    jmp    MJ_L21

    # while loop body
MJ_L20:

    # jump to while test
    jmp    MJ_L19

    # end of while
MJ_L21:
    jmp    MJ_L13

    # else label for if
MJ_L11:

    # done label for if
MJ_L13:

    # done label for if
MJ_L2:

    # Load constant int 1000
    ldi    r24,lo8(1000)
    ldi    r25,hi8(1000)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### Meggy.delay() call
    # load delay parameter
    # load a two byte expression off stack
    pop    r24
    pop    r25
    call   _Z8delay_msj


/* epilogue start */
    endLabel:
    jmp endLabel
    ret
    .size   main, .-main


