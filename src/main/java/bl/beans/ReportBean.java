package bl.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
;import java.io.Serializable;
import java.util.Date;

@Named
@SessionScoped
public class ReportBean implements Serializable {
    Date subDateStart;
    Date subDateFinish;
    Date otaDateStart;
    Date otaDateFinish;
    Date historyGDDateStart;
    Date historyGDDateFinish;

    public Date getOtaDateStart() {
        return otaDateStart;
    }

    public Date getHistoryGDDateStart() {
        return historyGDDateStart;
    }

    public void setHistoryGDDateStart(Date historyGDDateStart) {
        this.historyGDDateStart = historyGDDateStart;
    }

    public Date getHistoryGDDateFinish() {
        return historyGDDateFinish;
    }

    public void setHistoryGDDateFinish(Date historyGDDateFinish) {
        this.historyGDDateFinish = historyGDDateFinish;
    }

    public void setOtaDateStart(Date otaDateStart) {
        this.otaDateStart = otaDateStart;
    }

    public Date getOtaDateFinish() {
        return otaDateFinish;
    }

    public void setOtaDateFinish(Date otaDateFinish) {
        this.otaDateFinish = otaDateFinish;
    }

    public Date getSubDateStart() {
        return subDateStart;
    }

    public void setSubDateStart(Date subDateStart) {
        this.subDateStart = subDateStart;
    }

    public Date getSubDateFinish() {
        return subDateFinish;
    }

    public void setSubDateFinish(Date subDateFinish) {
        this.subDateFinish = subDateFinish;
    }
}
