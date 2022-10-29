package world.hzq.easysql.executor;

import world.hzq.easysql.executor.entity.ResultEntity;
import world.hzq.easysql.resolve.function.BranchHandle;
import world.hzq.easysql.resolve.sql.common.SQLEntity;
import world.hzq.easysql.resolve.sql.common.SQLLanguageType;
import world.hzq.easysql.resolve.sql.common.SQLResolver;
import world.hzq.easysql.resolve.sql.entity.CreateSQLEntity;

/**
 * 抽象执行器
 */
public abstract class AbstractExecutor implements Executor{
    //执行结果实体
    private ResultEntity resultEntity;

    /**
     * 以下lambda表达式为if else的替代
     */
    @Override
    public final void execute(SQLEntity sqlEntity){
        SQLLanguageType type = sqlEntity.getSqlLanguageType();
//        CreateSQLEntity createSQLEntity = (CreateSQLEntity) sqlEntity;
//        createSQLEntity.getType()
        BranchHandle.handle(SQLLanguageType.SELECT.equals(type))
                .execute(()->{
                    executeSelect(sqlEntity);
                },()->{
                    BranchHandle.handle(SQLLanguageType.CREATE.equals(type))
                            .execute(()->{
                                executeCreate(sqlEntity);
                            },()->{
                                BranchHandle.handle(SQLLanguageType.INSERT.equals(type))
                                        .execute(()->{
                                            executeInsert(sqlEntity);
                                        },()->{
                                            BranchHandle.handle(SQLLanguageType.UPDATE.equals(type))
                                                    .execute(()->{
                                                        executeUpdate(sqlEntity);
                                                    },()->{
                                                        BranchHandle.handle(SQLLanguageType.ORDER.equals(type))
                                                                .execute(()->{
                                                                    executeOrder(sqlEntity);
                                                                },()->{
                                                                    BranchHandle.handle(SQLLanguageType.ALTER.equals(type))
                                                                            .execute(()->{
                                                                                executeAlter(sqlEntity);
                                                                            },()->{
                                                                                BranchHandle.handle(SQLLanguageType.DROP.equals(type))
                                                                                        .execute(()->{
                                                                                            executeDrop(sqlEntity);
                                                                                        },()->{
                                                                                            BranchHandle.handle(SQLLanguageType.GRANT.equals(type))
                                                                                                    .execute(()->{
                                                                                                        executeGrant(sqlEntity);
                                                                                                    },()->{
                                                                                                        BranchHandle.handle(SQLLanguageType.REVOKE.equals(type))
                                                                                                                .execute(()->{
                                                                                                                    executeRevoke(sqlEntity);
                                                                                                                },()->{
                                                                                                                    BranchHandle.handle(SQLLanguageType.ROLLBACK.equals(type))
                                                                                                                            .execute(()->{
                                                                                                                                executeRollback(sqlEntity);
                                                                                                                            },()->{
                                                                                                                                BranchHandle.handle(SQLLanguageType.SAVEPOINT.equals(type))
                                                                                                                                        .execute(()->{
                                                                                                                                            executeSavePoint(sqlEntity);
                                                                                                                                        },()->{
                                                                                                                                            BranchHandle.handle(SQLLanguageType.COMMENT.equals(type))
                                                                                                                                                    .execute(()->{
                                                                                                                                                        executeComment(sqlEntity);
                                                                                                                                                    },()->{
                                                                                                                                                        BranchHandle.handle(SQLLanguageType.COMMIT.equals(type))
                                                                                                                                                                .execute(()->{
                                                                                                                                                                    executeCommit(sqlEntity);
                                                                                                                                                                },()->{
                                                                                                                                                                    throw new RuntimeException("sql execute error \n cause by : sql is illegal or not currently supported");
                                                                                                                                                                });
                                                                                                                                                    });
                                                                                                                                        });
                                                                                                                            });
                                                                                                                });
                                                                                                    });
                                                                                        });
                                                                            });
                                                                });
                                                    });
                                        });
                            });
                });
    }

    @Override
    public final void execute(SQLResolver resolver) {
        //获取解析器的解析结果
        SQLEntity sqlEntity = resolver.getSQLEntity();
        execute(sqlEntity);
    }

    //执行对应语句
    protected abstract void executeSelect(SQLEntity sqlEntity);

    protected abstract void executeCommit(SQLEntity sqlEntity);

    protected abstract void executeComment(SQLEntity sqlEntity);

    protected abstract void executeSavePoint(SQLEntity sqlEntity);

    protected abstract void executeRollback(SQLEntity sqlEntity);

    protected abstract void executeRevoke(SQLEntity sqlEntity);

    protected abstract void executeGrant(SQLEntity sqlEntity);

    protected abstract void executeDrop(SQLEntity sqlEntity);

    protected abstract void executeAlter(SQLEntity sqlEntity);

    protected abstract void executeOrder(SQLEntity sqlEntity);

    protected abstract void executeUpdate(SQLEntity sqlEntity);

    protected abstract void executeInsert(SQLEntity sqlEntity);

    protected abstract void executeCreate(SQLEntity sqlEntity);


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getResult(Class<T> resultEntityClass) {
        return (T) resultEntity;
    }

    @Override
    public ResultEntity getResult() {
        return resultEntity;
    }

    public void setResultEntity(ResultEntity resultEntity) {
        this.resultEntity = resultEntity;
    }
}
