package au.edu.unsw.soacourse.ors.beans;

public class AutoCheckResult {

    private String _autoCheckId;
    private String _appId;
    private String pdvResult;
    private String crvResult;
    
	public AutoCheckResult() {
		
	}

	public String get_autoCheckId() {
		return _autoCheckId;
	}

	public void set_autoCheckId(String _autoCheckId) {
		this._autoCheckId = _autoCheckId;
	}

	public String get_appId() {
		return _appId;
	}

	public void set_appId(String _appId) {
		this._appId = _appId;
	}

	public String getPdvResult() {
		return pdvResult;
	}

	public void setPdvResult(String pdvResult) {
		this.pdvResult = pdvResult;
	}

	public String getCrvResult() {
		return crvResult;
	}

	public void setCrvResult(String crvResult) {
		this.crvResult = crvResult;
	}
}
