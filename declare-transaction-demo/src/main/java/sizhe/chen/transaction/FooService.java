package sizhe.chen.transaction;

public interface FooService {
    void insertRecord();
    void insertThenRollBack() throws RollbackException;
    void invokeInsertThenRollBack() throws RollbackException;

}
