package world.hzq.easysql.resolve.function;

@FunctionalInterface
public interface BranchHandle {
    void execute(Runnable trueHandle,Runnable falseHandle);

    /**
     * 参数为true或false时，分别进行不同的操作
     **/
    static BranchHandle handle(boolean expression){
        return (trueHandle, falseHandle) -> {
            if (expression){
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }
}
