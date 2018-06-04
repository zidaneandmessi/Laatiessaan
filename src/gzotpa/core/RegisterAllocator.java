package gzotpa.core;
import gzotpa.ast.*;
import gzotpa.code.nasm.*;
import gzotpa.entity.*;
import gzotpa.exception.*;
import gzotpa.ir.*;
import gzotpa.type.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class RegisterAllocator {
    private final RegisterClass[] FREE_REGISTERS = {
        RegisterClass.R8, RegisterClass.R9, RegisterClass.R10,RegisterClass.R11,
        RegisterClass.R12, RegisterClass.R13, RegisterClass.R14, RegisterClass.R15
    };
    private int freeRegsCnt = 8;
}