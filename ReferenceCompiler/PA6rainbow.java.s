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


    # NewExp
    ldi    r24, lo8(2)
    ldi    r25, hi8(2)
    # allocating object of size 2 on heap
    call    malloc
    # push object address
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

    #### function call
    # put parameter values into appropriate registers
    # load a one byte expression off stack
    pop    r22
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    Rainbow_run


/* epilogue start */
    endLabel:
    jmp endLabel
    ret
    .size   main, .-main



    .text
.global Rainbow_run
    .type  Rainbow_run, @function
Rainbow_run:
    push   r29
    push   r28
    # make space for locals and params
    ldi    r30, 0
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
/* done with function Rainbow_run prologue */


    # Load constant int 8
    ldi    r24,lo8(8)
    ldi    r25,hi8(8)
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

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25

    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # store rhs into var p
    std    Z + 1, r25
    std    Z + 0, r24

    # IdExp
    # load value for variable p

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

    # Load constant int 0
    ldi    r24,lo8(0)
    ldi    r25,hi8(0)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.RED
    ldi    r22,1
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Load constant int 1
    ldi    r24,lo8(1)
    ldi    r25,hi8(1)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.ORANGE
    ldi    r22,2
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Load constant int 2
    ldi    r24,lo8(2)
    ldi    r25,hi8(2)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.YELLOW
    ldi    r22,3
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Color expression Meggy.Color.GREEN
    ldi    r22,4
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Load constant int 4
    ldi    r24,lo8(4)
    ldi    r25,hi8(4)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.BLUE
    ldi    r22,5
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Load constant int 5
    ldi    r24,lo8(5)
    ldi    r25,hi8(5)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.VIOLET
    ldi    r22,6
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Load constant int 6
    ldi    r24,lo8(6)
    ldi    r25,hi8(6)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.WHITE
    ldi    r22,7
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # IdExp
    # load value for variable p

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

    # Load constant int 7
    ldi    r24,lo8(7)
    ldi    r25,hi8(7)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Color expression Meggy.Color.DARK
    ldi    r22,0
    # push one byte expression onto stack
    push   r22

    ### ArrayAssignStatement
    # load rhs
    # load a one byte expression off stack
    pop    r24
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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

    # Load constant int 2
    ldi    r24,lo8(2)
    ldi    r25,hi8(2)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
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

    # IdExp
    # load value for variable p

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

    # Load constant int 0
    ldi    r24,lo8(0)
    ldi    r25,hi8(0)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### ArrayExp
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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
    # load array element and push onto stack
    ldd    r24, Z+0
    # push one byte expression onto stack
    push   r24

    ### Meggy.setPixel(x,y,color) call
    # load a one byte expression off stack
    pop    r20
    # load a one byte expression off stack
    pop    r22
    # load a one byte expression off stack
    pop    r24
    call   _Z6DrawPxhhh
    call   _Z12DisplaySlatev

    # Load constant int 2
    ldi    r24,lo8(2)
    ldi    r25,hi8(2)
    # push two byte expression onto stack
    push   r25
    push   r24

    # Casting int to byte by popping
    # 2 bytes off stack and only pushing low order bits
    # back on.  Low order bits are on top of stack.
    pop    r24
    pop    r25
    push   r24

    # Load constant int 4
    ldi    r24,lo8(4)
    ldi    r25,hi8(4)
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
    # load value for variable p

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

    # Load constant int 4
    ldi    r24,lo8(4)
    ldi    r25,hi8(4)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### ArrayExp
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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
    # load array element and push onto stack
    ldd    r24, Z+0
    # push one byte expression onto stack
    push   r24

    ### Meggy.setPixel(x,y,color) call
    # load a one byte expression off stack
    pop    r20
    # load a one byte expression off stack
    pop    r22
    # load a one byte expression off stack
    pop    r24
    call   _Z6DrawPxhhh
    call   _Z12DisplaySlatev


    # loading the implicit "this"

    # load a two byte variable from base+offset
    ldd    r31, Y + 2
    ldd    r30, Y + 1
    # push two byte expression onto stack
    push   r31
    push   r30

    # IdExp
    # load value for variable row
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

    # IdExp
    # load value for variable p

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

    #### function call
    # put parameter values into appropriate registers
    # load a two byte expression off stack
    pop    r20
    pop    r21
    # load a one byte expression off stack
    pop    r22
    # receiver will be passed as first param
    # load a two byte expression off stack
    pop    r24
    pop    r25

    call    Rainbow_displayRow

/* epilogue start for Rainbow_run */
    # no return value
    # pop space off stack for parameters and locals
    pop    r30
    pop    r30
    pop    r30
    # restoring the frame pointer
    pop    r28
    pop    r29
    ret
    .size Rainbow_run, .-Rainbow_run


    .text
.global Rainbow_displayRow
    .type  Rainbow_displayRow, @function
Rainbow_displayRow:
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

    # Copy stack pointer to frame pointer
    in     r28,__SP_L__
    in     r29,__SP_H__

    # save off parameters
    std    Y + 2, r25
    std    Y + 1, r24
    std    Y + 3, r22
    std    Y + 5, r21
    std    Y + 4, r20
/* done with function Rainbow_displayRow prologue */


    # Load constant int 0
    ldi    r24,lo8(0)
    ldi    r25,hi8(0)
    # push two byte expression onto stack
    push   r25
    push   r24

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var i
    std    Y + 7, r25
    std    Y + 6, r24

    #### while statement
MJ_L0:

    # IdExp
    # load value for variable i
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 7
    ldd    r24, Y + 6
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 8
    ldi    r24,lo8(8)
    ldi    r25,hi8(8)
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
    brlt MJ_L4

    # load false
MJ_L3:
    ldi     r24, 0
    jmp      MJ_L5

    # load true
MJ_L4:
    ldi    r24, 1

    # push result of less than
MJ_L5:
    # push one byte expression onto stack
    push   r24

    # if not(condition)
    # load a one byte expression off stack
    pop    r24
    ldi    r25,0
    cp     r24, r25
    # WANT breq MJ_L2
    brne   MJ_L1
    jmp    MJ_L2

    # while loop body
MJ_L1:

    # IdExp
    # load value for variable i
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 7
    ldd    r24, Y + 6
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
    # load value for variable row
    # variable is a local or param variable

    # load a one byte variable from base+offset
    ldd    r24, Y + 3
    # push one byte expression onto stack
    push   r24

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
    # load value for variable i
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 7
    ldd    r24, Y + 6
    # push two byte expression onto stack
    push   r25
    push   r24

    ### ArrayExp
    # calculate the array element address by first
    # loading index
    # load a two byte expression off stack
    pop    r18
    pop    r19
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
    # load array element and push onto stack
    ldd    r24, Z+0
    # push one byte expression onto stack
    push   r24

    ### Meggy.setPixel(x,y,color) call
    # load a one byte expression off stack
    pop    r20
    # load a one byte expression off stack
    pop    r22
    # load a one byte expression off stack
    pop    r24
    call   _Z6DrawPxhhh
    call   _Z12DisplaySlatev

    # IdExp
    # load value for variable i
    # variable is a local or param variable

    # load a two byte variable from base+offset
    ldd    r25, Y + 7
    ldd    r24, Y + 6
    # push two byte expression onto stack
    push   r25
    push   r24

    # Load constant int 1
    ldi    r24,lo8(1)
    ldi    r25,hi8(1)
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

    ### AssignStatement
    # load rhs exp
    # load a two byte expression off stack
    pop    r24
    pop    r25
    # store rhs into var i
    std    Y + 7, r25
    std    Y + 6, r24

    # jump to while test
    jmp    MJ_L0

    # end of while
MJ_L2:

/* epilogue start for Rainbow_displayRow */
    # no return value
    # pop space off stack for parameters and locals
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
    .size Rainbow_displayRow, .-Rainbow_displayRow

