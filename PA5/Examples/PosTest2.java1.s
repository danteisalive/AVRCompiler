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

    # Copy stack pointer to frame pointer
    in     r28,__SP_L__
    in     r29,__SP_H__

    # save off parameters
    std    Y + 2, r25
    std    Y + 1, r24
    std    Y + 3, r22
    std    Y + 5, r21
    std    Y + 4, r20
    std    Y + 6, r18

/* done with function A_mth1 prologue */


    #### short-circuited && operation
    # &&: left operand

    #### short-circuited && operation
    # &&: left operand

    #### short-circuited && operation
    # &&: left operand

    # IdExp
    # load value for variable b

    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # variable is a member variable

    # load a one byte variable from base+offset
    ldd    r24, Z + 4
    # push one byte expression onto stack
    push   r24

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L0
    brne  MJ_L1
    jmp   MJ_L0

MJ_L1:
    # right operand
    # load a one byte expression off stack
    pop    r24

    # True/1 expression
    ldi    r22, 1
    # push one byte expression onto stack
    push   r22

    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
MJ_L0:

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L2
    brne  MJ_L3
    jmp   MJ_L2

MJ_L3:
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
MJ_L2:

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L4
    brne  MJ_L5
    jmp   MJ_L4

MJ_L5:
    # right operand
    # load a one byte expression off stack
    pop    r24

    # IdExp
    # load value for variable b

    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # variable is a member variable

    # load a one byte variable from base+offset
    ldd    r24, Z + 4
    # push one byte expression onto stack
    push   r24

    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
MJ_L4:

    ### AssignStatement
    # load rhs exp
    # load a one byte expression off stack
    pop    r24
    # loading the implicit "this"
    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # store rhs into var b
    std    Z + 4, r24

    #### short-circuited && operation
    # &&: left operand

    # IdExp
    # load value for variable a
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 5
    ldd    r24, Y + 4
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 10
    ldi    r24,lo8(10)
    ldi    r25,hi8(10)
    # push two byte expression onto stack
    push   r25
    push   r24

    # less than expression
    # load a two byte expression off stack
    pop    r18
    pop    r19
    # load a two byte expression off stack
    pop    r24
    pop    r25
    cp    r24, r18
    cpc   r25, r19
    brlt MJ_L7

    # load false
MJ_L6:
    ldi     r24, 0
    jmp      MJ_L8

    # load true
MJ_L7:
    ldi    r24, 1

    # push result of less than
MJ_L8:
    # push one byte expression onto stack
    push   r24

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L9
    brne  MJ_L10
    jmp   MJ_L9

MJ_L10:
    # right operand
    # load a one byte expression off stack
    pop    r24

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    # Load constant int 500
    ldi    r24,lo8(500)
    ldi    r25,hi8(500)
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
    brlt     MJ_L14
    ldi    r25, 0
    jmp    MJ_L15
MJ_L14:
    ldi    r25, hi8(-1)
MJ_L15:
    cp    r24, r18
    cpc   r25, r19
    brlt MJ_L12
    # load false
MJ_L11:
    ldi     r24, 0
    jmp      MJ_L13
    # load true
MJ_L12:
    ldi    r24, 1
    # push result of less than
MJ_L13:
    # push one byte expression onto stack
    push   r24

    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
MJ_L9:

    ### AssignStatement
    # load rhs exp
    # load a one byte expression off stack
    pop    r24
    # loading the implicit "this"
    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # store rhs into var b
    std    Z + 4, r24

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
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

    # load a one byte expression off stack
    pop    r18
    # load a one byte expression off stack
    pop    r24
    # promoting a byte to an int
    tst     r24
    brlt     MJ_L16
    ldi    r25, 0
    jmp    MJ_L17
MJ_L16:
    ldi    r25, hi8(-1)
MJ_L17:
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L18
    ldi    r19, 0
    jmp    MJ_L19
MJ_L18:
    ldi    r19, hi8(-1)
MJ_L19:
    # Do add operation
    add    r24, r18
    adc    r25, r19
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

    # IdExp
    # load value for variable a
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 5
    ldd    r24, Y + 4
    # push two byte expression onto stack
    push   r25
    push   r24

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L20
    ldi    r19, 0
    jmp    MJ_L21
MJ_L20:
    ldi    r19, hi8(-1)
MJ_L21:
    # Do add operation
    add    r24, r18
    adc    r25, r19
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 10
    ldi    r24,lo8(10)
    ldi    r25,hi8(10)
    # push two byte expression onto stack
    push   r25
    push   r24

    # load a two byte expression off stack
    pop    r18
    pop    r19
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # Do add operation
    add    r24, r18
    adc    r25, r19
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 100
    ldi    r24,lo8(100)
    ldi    r25,hi8(100)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L22
    ldi    r19, 0
    jmp    MJ_L23
MJ_L22:
    ldi    r19, hi8(-1)
MJ_L23:
    # Do add operation
    add    r24, r18
    adc    r25, r19
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    # Load constant int 11
    ldi    r24,lo8(11)
    ldi    r25,hi8(11)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a one byte expression off stack
    pop    r24
    # promoting a byte to an int
    tst     r24
    brlt     MJ_L24
    ldi    r25, 0
    jmp    MJ_L25
MJ_L24:
    ldi    r25, hi8(-1)
MJ_L25:
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L26
    ldi    r19, 0
    jmp    MJ_L27
MJ_L26:
    ldi    r19, hi8(-1)
MJ_L27:

    # Do INT sub operation
    sub    r24, r18
    sbc    r25, r19
    # push hi order byte first
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

    # IdExp
    # load value for variable a
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 5
    ldd    r24, Y + 4
    # push two byte expression onto stack
    push   r25
    push   r24

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L28
    ldi    r19, 0
    jmp    MJ_L29
MJ_L28:
    ldi    r19, hi8(-1)
MJ_L29:

    # Do INT sub operation
    sub    r24, r18
    sbc    r25, r19
    # push hi order byte first
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 11
    ldi    r24,lo8(11)
    ldi    r25,hi8(11)
    # push two byte expression onto stack
    push   r25
    push   r24

    # load a two byte expression off stack
    pop    r18
    pop    r19
    # load a two byte expression off stack
    pop    r24
    pop    r25

    # Do INT sub operation
    sub    r24, r18
    sbc    r25, r19
    # push hi order byte first
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 12
    ldi    r24,lo8(12)
    ldi    r25,hi8(12)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L30
    ldi    r19, 0
    jmp    MJ_L31
MJ_L30:
    ldi    r19, hi8(-1)
MJ_L31:

    # Do INT sub operation
    sub    r24, r18
    sbc    r25, r19
    # push hi order byte first
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    # Load constant int 234
    ldi    r24,lo8(234)
    ldi    r25,hi8(234)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # MulExp
    # load a one byte expression off stack
    pop    r18
    # load a one byte expression off stack
    pop    r22
    # move low byte src into dest reg
    mov    r24, r18
    # move low byte src into dest reg
    mov    r26, r22
    # Do mul operation of two input bytes
    muls   r24, r26
    # push two byte expression onto stack
    push   r1
    push   r0
    # clear r0 and r1, thanks Brendan!
    eor    r0,r0
    eor    r1,r1

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

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

    # IdExp
    # load value for variable tempByte
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    ### Meggy.getPixel(x,y) call
    # load a one byte expression off stack
    pop    r22
    # load a one byte expression off stack
    pop    r24
    call   _Z6ReadPxhh
    # push one byte expression onto stack
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a one byte expression off stack
    pop    r24
    # store rhs into var c
    std    Y + 6, r24

    ### MeggyCheckButton
    call    _Z16CheckButtonsDownv
    lds    r24, Button_Right
    # if button value is zero, push 0 else push 1
    tst    r24
    breq   MJ_L32
MJ_L33:
    ldi    r24, 1
    jmp    MJ_L34
MJ_L32:
MJ_L34:
    # push one byte expression onto stack
    push   r24

    # False/0 expression
    ldi    r24,0
    # push one byte expression onto stack
    push   r24

    # equality check expression
    # load a one byte expression off stack
    pop    r18
    # load a one byte expression off stack
    pop    r24
    cp    r24, r18
    breq MJ_L36
    # result is false
MJ_L35:
    ldi     r24, 0
    jmp      MJ_L37
    # result is true
MJ_L36:
    ldi     r24, 1
    # store result of equal expression
MJ_L37:
    # push one byte expression onto stack
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a one byte expression off stack
    pop    r24
    # loading the implicit "this"
    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # store rhs into var b
    std    Z + 4, r24

    #### short-circuited && operation
    # &&: left operand

    #### short-circuited && operation
    # &&: left operand

    # False/0 expression
    ldi    r24,0
    # push one byte expression onto stack
    push   r24

    # not operation
    # load a one byte expression off stack
    pop    r24
    ldi     r22, 1
    eor     r24,r22
    # push one byte expression onto stack
    push   r24

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L38
    brne  MJ_L39
    jmp   MJ_L38

MJ_L39:
    # right operand
    # load a one byte expression off stack
    pop    r24

    # True/1 expression
    ldi    r22, 1
    # push one byte expression onto stack
    push   r22

    # not operation
    # load a one byte expression off stack
    pop    r24
    ldi     r22, 1
    eor     r24,r22
    # push one byte expression onto stack
    push   r24

    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
MJ_L38:

    # &&: if left operand is false do not eval right
    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
    # compare left exp with zero
    ldi r25, 0
    cp    r24, r25
    # Want this, breq MJ_L40
    brne  MJ_L41
    jmp   MJ_L40

MJ_L41:
    # right operand
    # load a one byte expression off stack
    pop    r24

    # IdExp
    # load value for variable b

    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # variable is a member variable

    # load a one byte variable from base+offset
    ldd    r24, Z + 4
    # push one byte expression onto stack
    push   r24

    # load a one byte expression off stack
    pop    r24
    # push one byte expression onto stack
    push   r24
MJ_L40:

    ### AssignStatement
    # load rhs exp
    # load a one byte expression off stack
    pop    r24
    # loading the implicit "this"
    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # store rhs into var b
    std    Z + 4, r24

    # IdExp
    # load value for variable aArray

    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # variable is a member variable

    # load a two byte variable from base+offset
    ldd    r25, Z + 3
    ldd    r24, Z + 2
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 10
    ldi    r24,lo8(10)
    ldi    r25,hi8(10)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 5
    ldi    r24,lo8(5)
    ldi    r25,hi8(5)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L42
    ldi    r19, 0
    jmp    MJ_L43
MJ_L42:
    ldi    r19, hi8(-1)
MJ_L43:
    # Do add operation
    add    r24, r18
    adc    r25, r19
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

    # IdExp
    # load value for variable cArray

    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # variable is a member variable

    # load a two byte variable from base+offset
    ldd    r25, Z + 1
    ldd    r24, Z + 0
    # push two byte expression onto stack
    push   r25
    push   r24

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

    # Load constant int 19
    ldi    r24,lo8(19)
    ldi    r25,hi8(19)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # load a one byte expression off stack
    pop    r18
    # load a one byte expression off stack
    pop    r24
    # promoting a byte to an int
    tst     r24
    brlt     MJ_L44
    ldi    r25, 0
    jmp    MJ_L45
MJ_L44:
    ldi    r25, hi8(-1)
MJ_L45:
    # promoting a byte to an int
    tst     r18
    brlt     MJ_L46
    ldi    r19, 0
    jmp    MJ_L47
MJ_L46:
    ldi    r19, hi8(-1)
MJ_L47:

    # Do INT sub operation
    sub    r24, r18
    sbc    r25, r19
    # push hi order byte first
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a one byte expression off stack
    pop    r24
    # store rhs into var c
    std    Y + 6, r24

    # IdExp
    # load value for variable BClass
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 8
    ldd    r24, Y + 7
    # push two byte expression onto stack
    push   r25
    push   r24

    #### function call
    # put parameter values into appropriate registers
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    B_func2

    # handle return value
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var a
    std    Y + 5, r25
    std    Y + 4, r24

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
    brlt     MJ_L48
    ldi    r25, 0
    jmp    MJ_L49
MJ_L48:
    ldi    r25, hi8(-1)
MJ_L49:
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
.global B_func2
    .type  B_func2, @function
B_func2:
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

/* done with function B_func2 prologue */


    # Load constant int 6
    ldi    r24,lo8(6)
    ldi    r25,hi8(6)
    # push two byte expression onto stack
    push   r25
    push   r24

/* epilogue start for B_func2 */
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
    .size B_func2, .-B_func2


