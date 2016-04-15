package tr.com.agem.arya.interpreter.script;

import android.view.View;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import tr.com.agem.arya.interpreter.components.AryaScript;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.base.AryaWindow;
import tr.com.agem.core.utils.AryaUtils;

public class ScriptHelper {
	
	private static final Logger logger = Logger.getLogger(ScriptHelper.class.getName());

	public static Object executeScript(String functionName, HashMap<Object, Object> params, AryaMain main) {

		StringBuilder script = new StringBuilder();
		AryaScript scriptObj = getScriptComponent(main.getAryaWindow());


		if (AryaUtils.isNotEmpty(scriptObj)) {
			script.append(scriptObj.getScript()).append("\n").append(getFunctionName(functionName));
			logger.log(Level.FINE, "Script: {0}", script);
			return JsRunner.jsRun(scriptObj.getSrcList(), script.toString(), main);
		}

		return null;
	}

	private static AryaScript getScriptComponent(AryaWindow aryaWindow) {
		View child = null;

		for (int i = 0 ; i < aryaWindow.getChildCount(); i++) {
			child = aryaWindow.getChildAt(i);
			if(child instanceof AryaScript) {
				return (AryaScript) child;
			}
		}
		return null;
	}

	private static StringBuilder getFunctionName(String functionName) {
		StringBuilder fName = new StringBuilder(functionName);
		if (!functionName.endsWith(")")) {
			fName.append("()");
		}
		return fName.append(";");
	}

}
