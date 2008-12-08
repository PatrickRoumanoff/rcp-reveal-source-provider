package com.roumanoff.debug.sourceprovider;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.ISourceProviderListener;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;
import org.eclipse.ui.swt.IFocusService;

/**
 * This view will display in alphabetical order all SourceProvider variable
 * available at the time of creation and will track overtime the evolution of
 * those values
 * 
 * @author Patrick Roumanoff
 * 
 */
public class SourceProviderView extends ViewPart {

	private IFocusService focusService;
	private ISourceProviderService sourceProviderService;
	private VariableComposite variableComposite;

	/**
	 * The default listener for all source provider.<br>
	 * it feeds the values back to the composite.
	 */
	private ISourceProviderListener listener = new ISourceProviderListener() {

		@SuppressWarnings("unchecked")
		public void sourceChanged(int sourcePriority, Map sourceValuesByName) {
			Map<String, Object> sourceValues = sourceValuesByName;
			for (String sourceName : sourceValues.keySet()) {
				sourceChanged(sourcePriority, sourceName, sourceValuesByName
						.get(sourceName));
			}
		}

		public void sourceChanged(int sourcePriority, String sourceName,
				Object sourceValue) {
			variableComposite.notifyUpdate(new SourceValue(sourcePriority,
					sourceName, sourceValue));
		}

	};

	public void createPartControl(Composite parent) {
		sourceProviderService = (ISourceProviderService) getSite().getService(
				ISourceProviderService.class);
		variableComposite = new VariableComposite(parent, SWT.NONE);
		subscribe();
		// the following is not necessary, it is just setup as an example of
		// source provider values
		getSite()
				.setSelectionProvider(variableComposite.getSelectionProvider());

		focusService = (IFocusService) getSite()
				.getService(IFocusService.class);
		focusService.addFocusTracker(variableComposite, this.getClass()
				.getName());
	}

	/**
	 * Does the actual work of getting the initial values and subscribing to
	 * each source provider
	 */
	@SuppressWarnings("unchecked")
	private void subscribe() {
		for (ISourceProvider sourceProvider : sourceProviderService
				.getSourceProviders()) {
			sourceProvider.addSourceProviderListener(listener);
			Map<String, Object> map = sourceProvider.getCurrentState();
			for (String key : map.keySet()) {
				variableComposite.notifyUpdate(new SourceValue(0, key, map
						.get(key)));
			}
		}

	}

	@Override
	public void dispose() {
		focusService.removeFocusTracker(variableComposite);
		for (ISourceProvider sourceProvider : sourceProviderService
				.getSourceProviders()) {
			sourceProvider.removeSourceProviderListener(listener);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		variableComposite.setFocus();
	}

}