package com.manager.service.intern;

import com.manager.form.EditInternParam;
import com.manager.vo.intern.IsPeriodVO;
import com.manager.vo.intern.PeriodListVO;

public interface InternService {

    boolean editPeriod(EditInternParam param);

    IsPeriodVO isPeriod(String studentId);

    PeriodListVO getPeriodList();
}
