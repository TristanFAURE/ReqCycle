package org.eclipse.reqcycle.traceability.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.reqcycle.dnd.DNDReqCycle;
import org.eclipse.reqcycle.traceability.builder.IBuildingTraceabilityEngine;
import org.eclipse.reqcycle.traceability.engine.ITraceabilityEngine.DIRECTION;
import org.eclipse.reqcycle.traceability.engine.Request;
import org.eclipse.reqcycle.traceability.engine.Request.DEPTH;
import org.eclipse.reqcycle.traceability.model.scopes.CompositeScope;
import org.eclipse.reqcycle.traceability.model.scopes.Scopes;
import org.eclipse.reqcycle.traceability.types.conditions.TypeConditions;
import org.eclipse.reqcycle.traceability.types.scopes.ConfigurationScope;
import org.eclipse.reqcycle.traceability.ui.Activator;
import org.eclipse.reqcycle.traceability.ui.TraceabilityUtils;
import org.eclipse.reqcycle.traceability.ui.providers.BusinessDeffered;
import org.eclipse.reqcycle.traceability.ui.providers.RequestContentProvider;
import org.eclipse.reqcycle.traceability.ui.providers.RequestLabelProvider;
import org.eclipse.reqcycle.traceability.ui.services.ILocateService;
import org.eclipse.reqcycle.types.IType;
import org.eclipse.reqcycle.types.ITypesManager;
import org.eclipse.reqcycle.types.ui.providers.IterableOfTypesContentProvider;
import org.eclipse.reqcycle.types.ui.providers.TypeLabelProvider;
import org.eclipse.reqcycle.uri.IReachableManager;
import org.eclipse.reqcycle.uri.exceptions.IReachableHandlerException;
import org.eclipse.reqcycle.uri.model.IObjectHandler;
import org.eclipse.reqcycle.uri.model.Reachable;
import org.eclipse.reqcycle.uri.model.ReachableObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.PluginTransferData;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.ziggurat.inject.ZigguratInject;

public class TraceabilityViewer extends ViewPart implements ISelectionListener {

	private static final String PLUGIN_ID = Activator.PLUGIN_ID;
	public static final String ID = "org.eclipse.reqcycle.traceability.ui.views.TraceabilityViewer"; //$NON-NLS-1$
	public static final String MENU_ID = ID + ".menu"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private Text targetText;
	private Set<Reachable> sources = new HashSet<Reachable>();
	private Object target;
	private ISelection selection;
	private TreeViewer traceabilityTreeViewer;
	private ComboViewer comboDirectionViewer;
	private DIRECTION direction;
	private RequestContentProvider contentProvider;
	private TreeViewer listOfTypesViewer;
	private ITypesManager manager = ZigguratInject.make(ITypesManager.class);
	private IReachableManager reachManager = ZigguratInject
			.make(IReachableManager.class);
	private ILocateService locateService = ZigguratInject
			.make(ILocateService.class);
	private Action delete_action;
	private Action refresh_action;
	private Action plus_action;
	private Action sync_action;
	private Action new_instance;
	private Action changeViewName;
	private Button btnFilterOnCurrent;
	private Action locateAction;

	public TraceabilityViewer() {
		setTitleImage(ResourceManager.getPluginImage(
				"org.eclipse.reqcycle.traceability.ui", "icons/path.gif"));
	}

	@Override
	public void dispose() {
		super.dispose();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().removeSelectionListener(this);
		contentProvider.dispose();
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().addSelectionListener(this);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Form frmNewForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("Traceability");
		frmNewForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite_3 = formToolkit.createComposite(
				frmNewForm.getBody(), SWT.NONE);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		contentProvider = new RequestContentProvider();
		RequestLabelProvider labelProvider = new RequestLabelProvider();
		ZigguratInject.inject(labelProvider, contentProvider);

		Composite compoTrac = formToolkit
				.createComposite(composite_3, SWT.NONE);
		compoTrac.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		formToolkit.paintBordersFor(compoTrac);
		compoTrac.setLayout(new FillLayout(SWT.HORIZONTAL));

		Section sctTraceability = formToolkit.createSection(compoTrac,
				Section.COMPACT | Section.EXPANDED | Section.TITLE_BAR);
		sctTraceability.setText("Path Tree");
		formToolkit.paintBordersFor(sctTraceability);

		Composite composite_1 = formToolkit.createComposite(sctTraceability,
				SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctTraceability.setClient(composite_1);
		composite_1.setLayout(new GridLayout(1, false));

		traceabilityTreeViewer = new TreeViewer(composite_1, SWT.BORDER
				| SWT.VIRTUAL);
		getSite().setSelectionProvider(traceabilityTreeViewer);
		createDropTarget(traceabilityTreeViewer.getTree());
		traceabilityTreeViewer.setUseHashlookup(true);
		traceabilityTreeViewer.setContentProvider(contentProvider);
		traceabilityTreeViewer.setLabelProvider(labelProvider);
		traceabilityTreeViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						Object firstElement = ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (firstElement instanceof Reachable) {
							Reachable reachable = (Reachable) firstElement;
							listOfTypesViewer.setInput(manager
									.getAllApplicableTypes(reachable));
						} else {
							listOfTypesViewer.setInput(null);
						}
					}
				});
		Tree treeTraceability = traceabilityTreeViewer.getTree();
		GridData gd_treeTraceability = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_treeTraceability.heightHint = 286;
		treeTraceability.setLayoutData(gd_treeTraceability);
		formToolkit.paintBordersFor(treeTraceability);

		MenuManager menuManager = new MenuManager();
		locateAction = new Action("Locate",
				ResourceManager.getPluginImageDescriptor(
						"org.eclipse.reqcycle.traceability.ui",
						"icons/locate.gif")) {
			@Override
			public void run() {
				Reachable reachable = getSelectedReachable();
				try {
					locateService.open(reachable);
				} catch (Exception e) {
					ErrorDialog.openError(
							Display.getDefault().getActiveShell(), "Error",
							e.getMessage(), Status.OK_STATUS);
				}
			}

		};
		menuManager.add(locateAction);
		menuManager.add(new Separator());
		menuManager.add(new Action("Show Properties view", ResourceManager
				.getPluginImageDescriptor(
						"org.eclipse.reqcycle.traceability.ui",
						"icons/properties-1.gif")) {
			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage()
							.showView("org.eclipse.ui.views.PropertySheet");
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}

		});
		Menu menu = menuManager.createContextMenu(treeTraceability);
		getSite().registerContextMenu(MENU_ID, menuManager,
				traceabilityTreeViewer);
		treeTraceability.setMenu(menu);

		traceabilityTreeViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						Object firstElement = ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (firstElement instanceof BusinessDeffered) {
							BusinessDeffered busi = (BusinessDeffered) firstElement;
							firstElement = busi.getBusinessElement();
						}
						if (firstElement instanceof Reachable) {
							Reachable reachable = (Reachable) firstElement;
							listOfTypesViewer.setInput(manager
									.getAllApplicableTypes(reachable));
						} else {
							listOfTypesViewer.setInput(null);
						}
					}
				});

		Composite compoParam = formToolkit.createComposite(composite_3,
				SWT.NONE);
		compoParam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 2));
		formToolkit.paintBordersFor(compoParam);
		compoParam.setLayout(new FillLayout(SWT.HORIZONTAL));

		Section sctnParameters = formToolkit.createSection(compoParam,
				Section.TWISTIE);
		formToolkit.paintBordersFor(sctnParameters);
		sctnParameters.setText("Parameters");

		Composite composite = formToolkit.createComposite(sctnParameters,
				SWT.NONE);
		formToolkit.paintBordersFor(composite);
		sctnParameters.setClient(composite);
		composite.setLayout(new GridLayout(5, false));

		Label lblTarget = formToolkit.createLabel(composite, "target :",
				SWT.NONE);
		lblTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		targetText = formToolkit.createText(composite, "New Text", SWT.NONE);
		targetText.setEditable(false);
		targetText.setText("");
		targetText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		createDropTarget(targetText);
		Button btnChooseType = formToolkit.createButton(composite, "...",
				SWT.NONE);
		btnChooseType.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				ElementTreeSelectionDialog d = new ElementTreeSelectionDialog(
						Display.getDefault().getActiveShell(),
						new TypeLabelProvider(),
						new IterableOfTypesContentProvider());
				d.setInput(manager.getAllTypes());
				if (d.open() == ElementTreeSelectionDialog.OK) {
					IType firstResult = (IType) d.getFirstResult();
					target = firstResult;
					targetText.setText(firstResult.getLabel());
				}
			}

		});
		Button btnAddTarget = formToolkit.createButton(composite, "", SWT.NONE);
		btnAddTarget.setImage(ResourceManager.getPluginImage(PLUGIN_ID,
				"icons/add_obj.gif"));
		btnAddTarget.addSelectionListener(new AddSelectionListener(
				new TargetSetter()));

		Button btnDeleteTarget = formToolkit.createButton(composite, "",
				SWT.NONE);
		btnDeleteTarget.setImage(ResourceManager.getPluginImage(PLUGIN_ID,
				"icons/delete_obj.gif"));
		btnDeleteTarget.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				target = null;
				targetText.setText("");
			}
		});

		Label lblDirection = formToolkit.createLabel(composite, "direction :",
				SWT.NONE);
		lblDirection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		comboDirectionViewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo combo = comboDirectionViewer.getCombo();
		comboDirectionViewer.setContentProvider(new ArrayContentProvider());
		comboDirectionViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				return super.getText(element.toString());
			}

		});
		comboDirectionViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						direction = (DIRECTION) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
					}
				});
		comboDirectionViewer.setInput(Arrays.asList(DIRECTION.UPWARD,
				DIRECTION.DOWNWARD));
		comboDirectionViewer.setSelection(new StructuredSelection(
				DIRECTION.DOWNWARD));
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4,
				1));
		formToolkit.paintBordersFor(combo);

		Label lblConfiguration = new Label(composite, SWT.NONE);
		lblConfiguration.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		formToolkit.adapt(lblConfiguration, true, true);
		lblConfiguration.setText("configuration :");

		btnFilterOnCurrent = new Button(composite, SWT.CHECK);
		btnFilterOnCurrent.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 4, 1));
		formToolkit.adapt(btnFilterOnCurrent, true, true);
		btnFilterOnCurrent.setText("Filter on current configuration");

		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.adapt(composite_4);
		formToolkit.paintBordersFor(composite_4);

		Section sctnTypesOfSelection = formToolkit.createSection(composite_4,
				Section.TWISTIE);
		formToolkit.paintBordersFor(sctnTypesOfSelection);
		sctnTypesOfSelection.setText("Type(s) of selection");

		Composite composite_2 = new Composite(sctnTypesOfSelection, SWT.NONE);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		sctnTypesOfSelection.setClient(composite_2);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		listOfTypesViewer = new TreeViewer(composite_2, SWT.BORDER
				| SWT.V_SCROLL);
		listOfTypesViewer
				.setContentProvider(new IterableOfTypesContentProvider());
		listOfTypesViewer.setLabelProvider(new TypeLabelProvider());
		createActions();
		initializeToolBar();
		initializeMenu();

	}

	private void createDropTarget(Control control) {
		DropTarget target = new DropTarget(control, DND.DROP_DEFAULT
				| DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
		target.setTransfer(new Transfer[] {
				LocalSelectionTransfer.getTransfer(),
				PluginTransfer.getInstance() });
		target.addDropListener(new DropTargetAdapter() {

			@Override
			public void dragEnter(DropTargetEvent event) {
				super.dragEnter(event);
				Widget widget = event.widget;
				if (widget instanceof DropTarget) {
					DropTarget drop = (DropTarget) widget;
					if (drop.getControl() != targetText) {
						// drop is forbidden when sync to selection is enabled
						if (isSyncToSelection()) {
							return;
						}
					}
				}
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (LocalSelectionTransfer.getTransfer().isSupportedType(
							event.dataTypes[i])) {
						event.currentDataType = event.dataTypes[i];
						break;
					}
				}
			}

			@Override
			public void drop(DropTargetEvent event) {
				super.drop(event);
				List<Reachable> reachables = new ArrayList<Reachable>();
				if (PluginTransfer.getInstance().isSupportedType(
						event.currentDataType)) {
					PluginTransferData ptd = (PluginTransferData) event.data;
					reachables = DNDReqCycle.getReachables(ptd.getData());
					if (!reachables.isEmpty()) {
						for (Reachable r : reachables) {
							if (event.item == targetText) {
								TargetSetter setter = new TargetSetter();
								setter.set(r);
							} else {

							}
						}
					}
				}
				if (LocalSelectionTransfer.getTransfer().isSupportedType(
						event.currentDataType)) {
					if (event.data instanceof IStructuredSelection) {
						IStructuredSelection structured = (IStructuredSelection) event.data;
						for (Iterator i = structured.iterator(); i.hasNext();) {
							Object o = i.next();
							Reachable r = getReachable(o);
							if (r != null) {
								reachables.add(r);
							}
						}
					}
				}
				Widget widget = event.widget;
				if (widget instanceof DropTarget) {
					DropTarget drop = (DropTarget) widget;
					ISetter setter = null;
					if (drop.getControl() == targetText) {
						setter = new TargetSetter();
					} else if (!isSyncToSelection()) {
						setter = new SourceSetter();
					}
					if (setter != null) {
						if (reachables != null && !reachables.isEmpty()) {
							for (Reachable r : reachables) {
								setter.set(r);
							}
						} else {
							setter.set(null);
						}
					}
				}

			}

			@Override
			public void dragOver(DropTargetEvent event) {
				super.dragOver(event);
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
				super.dropAccept(event);
			}

		});
	}

	public void setInput() {
		if (sources.isEmpty()) {
			traceabilityTreeViewer.setInput(null);
		} else {
			CompositeScope scope = new CompositeScope();
			scope.add(Scopes.getWorkspaceScope());
			scope.add(new ConfigurationScope());
			Request request = new Request()
					.setDirection(direction)
					.setScope(scope)
					.setDepth(DEPTH.ONE)
					.addProperty(
							IBuildingTraceabilityEngine.OPTION_CHECK_CACHE,
							true)
					.addProperty(RequestContentProvider.CONF_KEY,
							btnFilterOnCurrent.getSelection());
			if (target == null) {
				for (Reachable r : sources) {
					request.addSource(r);
				}
			} else {
				if (target instanceof Reachable) {
					for (Reachable r : sources) {
						request.addSourceAndTarget(r, (Reachable) target);
					}
				} else if (target instanceof IType) {
					for (Reachable r : sources) {
						request.addSourceAndCondition(r,
								TypeConditions.is((IType) target));
					}
				}
				request.setDepth(DEPTH.INFINITE);
			}
			traceabilityTreeViewer.setInput(request);
		}
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			delete_action = new Action("Remove selections") {

				@Override
				public void run() {
					runDelete();
				}

			};
			delete_action.setImageDescriptor(ResourceManager
					.getPluginImageDescriptor(
							"org.eclipse.reqcycle.traceability.ui",
							"icons/delete_obj.gif"));
		}
		{
			refresh_action = new Action("Refresh view") {

				@Override
				public void run() {
					runRefresh();
				}

			};
			refresh_action.setImageDescriptor(ResourceManager
					.getPluginImageDescriptor(
							"org.eclipse.reqcycle.traceability.ui",
							"icons/update.gif"));
		}
		{
			plus_action = new Action("Add current selection") {

				@Override
				public void run() {
					handleCurrentSelection(new SourceSetter());
				}

			};
			plus_action.setImageDescriptor(ResourceManager
					.getPluginImageDescriptor(
							"org.eclipse.reqcycle.traceability.ui",
							"icons/add_obj.gif"));
		}
		{
			sync_action = new Action("Sync to selection", SWT.TOGGLE) {

				@Override
				public void run() {
					runSync();
				}

			};
			sync_action.setImageDescriptor(ResourceManager
					.getPluginImageDescriptor(
							"org.eclipse.reqcycle.traceability.ui",
							"icons/synced-1.gif"));
		}
		{
			new_instance = new Action("New Instance") {

				@Override
				public void run() {
					createNewView();
				}

			};
			new_instance.setImageDescriptor(ResourceManager
					.getPluginImageDescriptor(
							"org.eclipse.reqcycle.traceability.ui",
							"icons/newView.gif"));
		}
		{
			changeViewName = new Action("Change view name") {

				@Override
				public void run() {
					setViewName();
				}

			};
			changeViewName.setImageDescriptor(ResourceManager
					.getPluginImageDescriptor(
							"org.eclipse.reqcycle.traceability.ui",
							"icons/setName.gif"));
		}
	}

	protected void setViewName() {
		InputDialog dialog = new InputDialog(getSite().getShell(), "View Name",
				"Please enter the name of this view", getPartName(), null);
		if (dialog.open() == InputDialog.OK) {
			String value = dialog.getValue();
			if (value != null && value.length() > 0) {
				setPartName(value);
			}
		}
	}

	protected void createNewView() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		int nbView = 0;
		for (IViewReference ref : activePage.getViewReferences()) {
			if (ref.getId().startsWith(ID)) {
				nbView++;
			}
		}
		// increment to have the second view named #2
		nbView++;
		try {
			IViewPart view = activePage.showView(ID, ID + "_" + nbView,
					IWorkbenchPage.VIEW_ACTIVATE);
			// view.
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(sync_action);
		toolbarManager.add(plus_action);
		toolbarManager.add(refresh_action);
		toolbarManager.add(delete_action);
		toolbarManager.add(new_instance);
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
		menuManager.add(sync_action);
		menuManager.add(plus_action);
		menuManager.add(refresh_action);
		menuManager.add(delete_action);
		menuManager.add(new_instance);
		menuManager.add(changeViewName);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private class AddSelectionListener implements SelectionListener {

		private ISetter setter;

		public AddSelectionListener(ISetter s) {
			this.setter = s;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			handleCurrentSelection(setter);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}

	private interface ISetter {
		void set(Reachable r);
	}

	private class SourceSetter implements ISetter {
		@Override
		public void set(Reachable r) {
			sources.add(r);
			setInput();
		}
	}

	private class TargetSetter implements ISetter {

		public void set(Reachable r) {
			if (r == null) {
				targetText.setText("invalid selection");
				target = null;
			} else {
				target = r;
				targetText.setText(TraceabilityUtils.getText(r));
			}

		}
	}

	public Reachable getReachable(Object o) {
		try {
			IObjectHandler handler = reachManager.getHandlerFromObject(o);
			ReachableObject reachableObject = handler.getFromObject(o);
			if (reachableObject != null) {
				return reachableObject.getReachable(o);
			}
		} catch (IReachableHandlerException e) {
		}
		return null;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part != this && (!(part instanceof PropertySheet))) {
			this.selection = selection;
			if (isSyncToSelection()) {
				sources.clear();
				handleCurrentSelection(new SourceSetter());
				setInput();
				if (isSyncToSelection()) {
					traceabilityTreeViewer.expandToLevel(TreeViewer.ALL_LEVELS);
				}
			}
		} else if (part == this) {
			locateAction.setEnabled(locateService
					.isOpenable(getSelectedReachable()));
		}
	}

	public boolean isSyncToSelection() {
		return sync_action.isChecked();
	}

	private void handleCurrentSelection(ISetter setter) {
		if (selection != null && !selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structured = (IStructuredSelection) selection;
				for (Iterator<Object> i = structured.iterator(); i.hasNext();) {
					Object o = i.next();
					if (o == null) {
						setter.set(null);
					} else {
						setter.set(getReachable(o));
					}

				}
			}
		}
	}

	private void runRefresh() {
		IStructuredSelection sel = (IStructuredSelection) comboDirectionViewer
				.getSelection();
		if (sel != null && !sel.isEmpty() && !sources.isEmpty()) {
			setInput();
		}
	}

	private void runDelete() {
		sources.clear();
		setInput();
	}

	private void runSync() {
		boolean enabled = sync_action.isChecked();
		sync_action.setChecked(enabled);
		traceabilityTreeViewer.setData(RequestContentProvider.EXPAND_ALL,
				String.valueOf(enabled));
	}

	public void refresh() {
		traceabilityTreeViewer.refresh(true);
	}

	public void refreshElement(Object o) {
		traceabilityTreeViewer.update(o, null);
	}

	private Reachable getSelectedReachable() {
		ISelection selec = traceabilityTreeViewer.getSelection();
		if (selec instanceof IStructuredSelection) {
			IStructuredSelection structured = (IStructuredSelection) selec;
			if (structured.getFirstElement() instanceof BusinessDeffered) {
				BusinessDeffered busi = (BusinessDeffered) structured
						.getFirstElement();
				if (busi.getBusinessElement() instanceof Reachable) {
					Reachable reach = (Reachable) busi.getBusinessElement();
					return reach;
				}
			}
		}
		return null;
	}
}
