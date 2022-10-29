package world.hzq.easysql.executor.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SelectResultEntity extends ResultEntity{
    private List<String> queryList;
    private final List<Row> rowList = new LinkedList<>();
    private int[] maxLength;

    /**
     * 获取查询结果
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getResult(Class<T> targetClass) {
        return (T) convert();
    }

    @Override
    public String getResult() {
        return convert();
    }

    public void setQueryList(List<String> queryList) {
        String[] split = queryList.get(0).split(",");
        this.queryList = new ArrayList<>();
        this.queryList.addAll(Arrays.asList(split));
    }

    private String convert() {
        for (int i = 0; i < queryList.size(); i++) {
            maxLength[i] = Math.max(maxLength[i],queryList.get(i).length());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("+--");
        for (int i = 0; i < maxLength.length; i++) {
            for (int j = 0; j < maxLength[i]; j++) {
                sb.append("-");
            }
            sb.append("--+");
        }
        sb.append("\n");
        sb.append("|");
        for (String queryField : queryList) {
            sb.append(" ").append(queryField).append(" |");
        }
        sb.append("\n");
        sb.append("|");
        for (int i = 0; i < rowList.size(); i++) {
            for (String colValue : rowList.get(i).getColList()) {
                sb.append("  ").append(colValue).append(" |");
            }
            if(i != rowList.size() - 1){
                sb.append("\n");
                sb.append("|");
            }
        }
        sb.append("\n");
        sb.append(rowList.size()).append(" rows in set (0.00 sec)");
        return sb.toString();
    }

    public void addRow(Row row){
        if(maxLength == null){
            maxLength = new int[queryList.size()];
        }
        for (int i = 0; i < row.getColList().size(); i++) {
            maxLength[i] = Math.max(maxLength[i],row.getColList().get(i).length());
        }
        rowList.add(row);
    }

    public static class Row{
        private List<String> colList;
        public void setColList(List<String> colValueList){
            if (colValueList != null) {
                colList = new ArrayList<>(colValueList.size());
                colList.addAll(colValueList);
            }
        }

        public List<String> getColList() {
            return colList;
        }
    }
}
