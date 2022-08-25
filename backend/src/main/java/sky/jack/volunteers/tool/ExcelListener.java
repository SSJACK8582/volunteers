package sky.jack.volunteers.tool;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener {
    private List<Object> list = new ArrayList<>();

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        list.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
