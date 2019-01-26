package cn.buildworld.fileupload.entity;

public class Result {


    private boolean flag;
    private Integer code;
    private String msg;
    private Object data;
    private Long count;

    public Result(boolean flag, Integer code, String msg, Long count, Object data) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public Result(boolean flag, Integer code, String msg, Object data) {
        super();
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean flag, Integer code, String msg) {
        super();
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
