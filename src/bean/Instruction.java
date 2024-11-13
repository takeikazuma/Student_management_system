package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Instruction implements Serializable {

    private int instructionId;
    private LocalDate inputDate;
    private int usersId;
    private int studentId;
    private String instructions;
    private ZonedDateTime regDate;  // テーブルの datetime 型に対応
    private ZonedDateTime updateDate;  // テーブルの datetime 型に対応

    // instructionId の getter と setter
    public int getInstructionId() {
        return instructionId;
    }
    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    // inputDate の getter と setter
    public LocalDate getInputDate() {
        return inputDate;
    }
    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    // usersId の getter と setter
    public int getUsersId() {
        return usersId;
    }
    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    // studentId の getter と setter
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // instructions の getter と setter
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // regDate の getter と setter
    public ZonedDateTime getRegDate() {
        return regDate;
    }
    public void setRegDate(ZonedDateTime regDate) {
        this.regDate = regDate;
    }

    // updateDate の getter と setter
    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
