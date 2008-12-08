package com.roumanoff.debug.sourceprovider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Display a list of SourceValue in alphabetical order.<br>
 * the control get notified of new through call to the notifyUpdate method
 * 
 * @author Patrick Roumanoff
 * 
 */
public class VariableComposite extends Composite {

	private TableViewer viewer;
	private List<SourceValue> list = new ArrayList<SourceValue>();

	public VariableComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FillLayout());
		viewer = new TableViewer(this, SWT.SINGLE | SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new SourceLabelProvider());
		viewer.setComparator(new ViewerComparator());
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn tc = new TableColumn(table, SWT.NONE);
		tc.setText("Name");
		tc.setWidth(200);
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Value");
		tc.setWidth(300);
		tc = new TableColumn(table, SWT.NONE);
		tc.setText("Priority");
		tc.setWidth(100);
		viewer.setInput(list);
	}

	/**
	 * either add the new value to the list or update current value if already
	 * present
	 */
	public void notifyUpdate(SourceValue source) {
		for (SourceValue s : list) {
			if (s.name.equals(source.name)) {
				if(source.name.equals("selection") && source.getValue().startsWith("[[selection:")) {
					return; // avoid infinite loop. the current selection is the selection variable !
				}
				s.priority = source.priority;
				s.value = source.value;
				viewer.refresh();
				return;
			}
		}
		list.add(source);
		viewer.refresh();
	}

	/** display a value across three column: name, value and priority */
	class SourceLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			SourceValue source = (SourceValue) obj;
			switch (index) {
			case 0:
				return source.getName();
			case 1:
				return source.getValue();
			case 2:
				return source.getPriority();
			default:
				return "#N/A";
			}
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	/**
	 * expose the viewer as a selection provider<br>
	 * There is no real need for this one except to demo the current selection
	 * variable on itself
	 */
	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}

}
