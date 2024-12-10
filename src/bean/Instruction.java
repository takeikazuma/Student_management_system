package bean;

import java.io.Serializable;
import java.sql.Date;
import java.time.ZonedDateTime;

public class Instruction implements Serializable {

    private int instructionId;
    private Date inputDate;
    private int usersId;
    private int studentId;

    private String instructions;
    private ZonedDateTime regDate;  // テーブルの datetime 型に対応
    private ZonedDateTime updateDate;  // テーブルの datetime 型に対応

    private String usersName;	//JOINして取得したユーザ名（暫定コード）
    private String studentName;	//JOINして取得した学生氏名（暫定コード）

    // instructionId の getter と setter
    public int getInstructionId() {
        return instructionId;
    }
    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    // inputDate の getter と setter
    public Date getInputDate() {
        return inputDate;
    }
    public void setInputDate(Date inputDate) {
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

    public ZonedDateTime getRegDate(){
		return  regDate;
	}
	public void setRegDate(ZonedDateTime regDate) {
		this.regDate = regDate;
	}

	public ZonedDateTime getUpdateDate(){
		return  updateDate;
	}
	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}

    //SQLでJOINしたユーザ名アクセス用の暫定コード
	public String getUsersName() {
		return usersName;
	}
	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}




}
