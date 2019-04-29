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

    # NewExp
    ldi    r24, lo8(null)
    ldi    r25, hi8(null)
    # allocating object of size null on heap
    call    malloc
    # push object address
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 10
    ldi    r24,lo8(10)
    ldi    r25,hi8(10)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### NewArrayExp, allocating a new array
    # loading array size in elements
    # load a two byte expression off stack
    pop    r26
    pop    r27
    # since num elems might be in caller-saved registers
    # need to push num elems onto the stack around call to malloc
    # if had three-address code reg alloc could work around this
    push   r27
    push   r26
    # need bytes for elems + 2 in bytes
    ldi    r24, 2
    ldi    r25, 0
    add    r24,r26
    adc    r25,r27
    # call malloc, it expects r25:r24 as input
    call   malloc
    # set .length field to number of elements
    mov    r31, r25
    mov    r30, r24
    pop    r26
    pop    r27
    std    Z+0,r26
    std    Z+1,r27
    # store object address
    # push two byte expression onto stack
    push   r31
    push   r30

    # Load constant int 20
    ldi    r24,lo8(20)
    ldi    r25,hi8(20)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### NewArrayExp, allocating a new array
    # loading array size in elements
    # load a two byte expression off stack
    pop    r26
    pop    r27
    # since num elems might be in caller-saved registers
    # need to push num elems onto the stack around call to malloc
    # if had three-address code reg alloc could work around this
    push   r27
    push   r26
    # add size in elems to self to multiply by 2
    # complements of Jason Mealler
    add    r26,r26
    adc    r27,r27
    # need bytes for elems + 2 in bytes
    ldi    r24, 2
    ldi    r25, 0
    add    r24,r26
    adc    r25,r27
    # call malloc, it expects r25:r24 as input
    call   malloc
    # set .length field to number of elements
    mov    r31, r25
    mov    r30, r24
    pop    r26
    pop    r27
    std    Z+0,r26
    std    Z+1,r27
    # store object address
    # push two byte expression onto stack
    push   r31
    push   r30

    # True/1 expression
    ldi    r22, 1
    # push one byte expression onto stack
    push   r22

    # Load constant int 6
    ldi    r24,lo8(6)
    ldi    r25,hi8(6)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # Load constant int 12
    ldi    r24,lo8(12)
    ldi    r25,hi8(12)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.BLUE
    ldi    r22,5
    # push one byte expression onto stack
    push   r22

    #### function call
    # put parameter values into appropriate registers
    # load a one byte expression off stack
    pop    r12
    # load a two byte expression off stack
    pop    r14
    pop    r15
    # load a one byte expression off stack
    pop    r16
    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r20
    pop    r21
    # load a two byte expression off stack
    pop    r22
    pop    r23
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    A_mth1

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

    #### while statement

MJ_L11:
    # True/1 expression
    ldi    r22, 1
    # push one byte expression onto stack
    push   r22

    # if not(condition)
    # load a one byte expression off stack
    pop    r24
    ldi    r25,0
    cp     r24, r25
    # WANT breq MJ_L13
    brne   MJ_L12
    jmp    MJ_L13
    # while loop body
MJ_L12:

    # NewExp
    ldi    r24, lo8(null)
    ldi    r25, hi8(null)
    # allocating object of size null on heap
    call    malloc
    # push object address
    # push two byte expression onto stack
    push   r25
    push   r24

    #### function call
    # put parameter values into appropriate registers
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    B_mth2

    # jump to while test
    jmp    MJ_L11
    # end of while
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


    .text
.global A_mth1
    .type  A_mth1, @function
A_mth1:
    push   r29
    push   r28
    # make space for locals and params
    ldi    r30, 0
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30

    # Copy stack pointer to frame pointer
    in     r28,__SP_L__
    in     r29,__SP_H__

    # save off parameters
    std    Y + 2, r25
    std    Y + 1, r24
    std    Y + 4, r23
    std    Y + 3, r22
    std    Y + 6, r21
    std    Y + 5, r20
    std    Y + 7, r18
    std    Y + 8, r16
    std    Y + 10, r15
    std    Y + 9, r14
    std    Y + 11, r12

/* done with function A_mth1 prologue */


    # NewExp
    ldi    r24, lo8(null)
    ldi    r25, hi8(null)
    # allocating object of size null on heap
    call    malloc
    # push object address
    # push two byte expression onto stack
    push   r25
    push   r24

    # IdExp
    # load value for variable s
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y+1
    ldd    r24, Y+0
    # push two byte expression onto stack
    push   r25
    push   r24

    # IdExp
    # load value for variable e
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y+1
    ldd    r24, Y+0
    # push two byte expression onto stack
    push   r25
    push   r24

    # IdExp
    # load value for variable u
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y+0
    # push one byte expression onto stack
    push   r24

    # IdExp
    # load value for variable p
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y+0
    # push one byte expression onto stack
    push   r24

    # IdExp
    # load value for variable k
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y+1
    ldd    r24, Y+0
    # push two byte expression onto stack
    push   r25
    push   r24

    # IdExp
    # load value for variable r
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y+0
    # push one byte expression onto stack
    push   r24

    #### function call
    # put parameter values into appropriate registers
    # load a one byte expression off stack
    pop    r12
    # load a two byte expression off stack
    pop    r14
    pop    r15
    # load a one byte expression off stack
    pop    r16
    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r20
    pop    r21
    # load a two byte expression off stack
    pop    r22
    pop    r23
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    A_mth1

    # handle return value
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var d
    std    Y + 1, r25
    std    Y + 0, r24

    # IdExp
    # load value for variable t
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y+1
    ldd    r24, Y+0
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 6
    ldi    r24,lo8(6)
    ldi    r25,hi8(6)
    # push two byte expression onto stack
    push   r25
    push   r24

    # NewExp
    ldi    r24, lo8(null)
    ldi    r25, hi8(null)
    # allocating object of size null on heap
    call    malloc
    # push object address
    # push two byte expression onto stack
    push   r25
    push   r24

    #### function call
    # put parameter values into appropriate registers
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    B_mth2

    # handle return value
    # push two byte expression onto stack
    push   r25
    push   r24

    ### ArrayAssignStatement
    # load rhs
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
    # add size in elems to self to multiply by 2
    # complements of Jason Mealler
    add    r18,r18
    adc    r19,r19
    # put index*(elem size in bytes) into r31:r30
    mov    r31, r19
    mov    r30, r18
    # want result of addressing arithmetic 
    # to be in r31:r30 for access through Z
    # index over length
    ldi    r20, 2
    ldi    r21, 0
    add    r30, r20
    adc    r31, r21
    # loading array reference
    # load a two byte expression off stack
    pop    r22
    pop    r23
    # add array reference to result of indexing arithmetic
    add    r30, r22
    adc    r31, r23
    # store rhs into memory location for array element
    std    Z+0, r24
    std    Z+1, r25

    # Load constant int 100
    ldi    r24,lo8(100)
    ldi    r25,hi8(100)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### NewArrayExp, allocating a new array
    # loading array size in elements
    # load a two byte expression off stack
    pop    r26
    pop    r27
    # since num elems might be in caller-saved registers
    # need to push num elems onto the stack around call to malloc
    # if had three-address code reg alloc could work around this
    push   r27
    push   r26
    # need bytes for elems + 2 in bytes
    ldi    r24, 2
    ldi    r25, 0
    add    r24,r26
    adc    r25,r27
    # call malloc, it expects r25:r24 as input
    call   malloc
    # set .length field to number of elements
    mov    r31, r25
    mov    r30, r24
    pop    r26
    pop    r27
    std    Z+0,r26
    std    Z+1,r27
    # store object address
    # push two byte expression onto stack
    push   r31
    push   r30

/* epilogue start for A_mth1 */
    # handle return value
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # pop space off stack for parameters and locals
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    # restoring the frame pointer
    pop    r28
    pop    r29
    ret
    .size A_mth1, .-A_mth1


    .text
.global A_mth3
    .type  A_mth3, @function
A_mth3:
    push   r29
    push   r28
    # make space for locals and params
    ldi    r30, 0
    push   r30
    push   r30
    push   r30
    push   r30
    push   r30

    # Copy stack pointer to frame pointer
    in     r28,__SP_L__
    in     r29,__SP_H__

    # save off parameters
    std    Y + 2, r25
    std    Y + 1, r24
    std    Y + 4, r23
    std    Y + 3, r22
    std    Y + 5, r20

/* done with function A_mth3 prologue */


    # Color expression Meggy.Color.RED
    ldi    r22,1
    # push one byte expression onto stack
    push   r22

/* epilogue start for A_mth3 */
    # handle return value
    # load a one byte expression off stack
    pop    r24
    # promoting a byte to an int
    tst     r24
    brlt     MJ_L14
    ldi    r25, 0
    jmp    MJ_L15
MJ_L14:
    ldi    r25, hi8(-1)
MJ_L15:
    # pop space off stack for parameters and locals
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    pop    r30
    # restoring the frame pointer
    pop    r28
    pop    r29
    ret
    .size A_mth3, .-A_mth3


    .text
.global B_mth2
    .type  B_mth2, @function
B_mth2:
    push   r29
    push   r28
    # make space for locals and params
    ldi    r30, 0
    push   r30
    push   r30

    # Copy stack pointer to frame pointer
    in     r28,__SP_L__
    in     r29,__SP_H__

    # save off parameters
    std    Y + 2, r25
    std    Y + 1, r24

/* done with function B_mth2 prologue */


    # Load constant int 6
    ldi    r24,lo8(6)
    ldi    r25,hi8(6)
    # push two byte expression onto stack
    push   r25
    push   r24

/* epilogue start for B_mth2 */
    # handle return value
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # pop space off stack for parameters and locals
    pop    r30
    pop    r30
    # restoring the frame pointer
    pop    r28
    pop    r29
    ret
    .size B_mth2, .-B_mth2


