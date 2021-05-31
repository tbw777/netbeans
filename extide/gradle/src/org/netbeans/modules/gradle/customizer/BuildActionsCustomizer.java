/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.gradle.customizer;

import org.netbeans.modules.gradle.api.execute.ActionMapping;
import org.netbeans.modules.gradle.spi.actions.ProjectActionMappingProvider;
import org.netbeans.modules.gradle.execute.GradleCliEditorKit;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gradle.actions.CustomActionRegistrationSupport;
import org.netbeans.modules.gradle.api.execute.GradleExecConfiguration;
import org.netbeans.modules.gradle.configurations.ConfigurationSnapshot;
import org.netbeans.spi.project.ActionProvider;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Laszlo Kishalmi
 */
@Messages("TXT_CUSTOM=Custom...")
public class BuildActionsCustomizer extends javax.swing.JPanel {

    private final static String CUSTOM_ACTION = Bundle.TXT_CUSTOM();
    private static final String CARD_NOSELECT = "empty"; //NOI18N
    private static final String CARD_DETAILS = "details"; //NOI18N

    final Project project;
    final ConfigurationSnapshot configSnapshot;
    final DefaultComboBoxModel<GradleExecConfiguration> configModel = new DefaultComboBoxModel<>();
    final DefaultListModel<CustomActionMapping> customActionsModel = new DefaultListModel<>();
    final DefaultComboBoxModel<String> availableActionsModel = new DefaultComboBoxModel<>();
    final CustomActionRegistrationSupport actionRegistry;

    final DocumentListener applyListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            apply();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            apply();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            apply();
        }
    };

    private volatile boolean autoApply;
    private boolean comboReady;

    /**
     * Creates new form BuildActionsCustomizer
     */
    public BuildActionsCustomizer(Project project, ConfigurationSnapshot snapshot) {
        this.project = project;
        this.configSnapshot = snapshot;
        
        initComponents();
        actionRegistry = new CustomActionRegistrationSupport(project);
        lsActions.setCellRenderer(new MyListCellRenderer());
        tfLabel.getDocument().addDocumentListener(applyListener);
        EditorKit kit = CloneableEditorSupport.getEditorKit(GradleCliEditorKit.MIME_TYPE);
        taArgs.setEditorKit(kit);
        taArgs.getDocument().putProperty(Document.StreamDescriptionProperty, project);
        taArgs.getDocument().addDocumentListener(applyListener);
        initDefaultModels();
        comboReady = true;
        
        cbConfiguration.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (!(value instanceof GradleExecConfiguration) || !(c instanceof JLabel)) {
                    return c;
                }
                JLabel l = (JLabel)c;
                GradleExecConfiguration conf = (GradleExecConfiguration)value;
                l.setText(conf.getDisplayName());
                if (conf.equals(configSnapshot.getActiveConfiguration())) {
                    l.setFont(l.getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });
        cbConfiguration.setModel(configModel);
        cbConfiguration.addActionListener(this::configurationChanged);
    }
    
    private void configurationChanged(ActionEvent e) {
        if (!comboReady) {
            return;
        }
        GradleExecConfiguration cfg = (GradleExecConfiguration)cbConfiguration.getSelectedItem();
        if (cfg == null) {
            // will recursively fire here
            cbConfiguration.setSelectedItem(configSnapshot.getActiveConfiguration());
            return;
        }
        actionRegistry.setActiveConfiguration(cfg);
        initDefaultModels();
    }

    private void initDefaultModels() {
        boolean saved = comboReady;
        try {
            comboReady = false;
            ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);

            Set<String> allAvailableActions = new TreeSet<>(Arrays.asList(actionProvider.getSupportedActions()));

            customActionsModel.removeAllElements();
            actionRegistry.getCustomActions().forEach((CustomActionMapping mapping) -> {
                customActionsModel.addElement(mapping);
            });
            availableActionsModel.removeAllElements();
            availableActionsModel.addElement(CUSTOM_ACTION);

            // Add those actions to the combo box which were not customized yet.
            for (String action : allAvailableActions) {
                if (actionRegistry.getCustomAction(action) == null) {
                    availableActionsModel.addElement(action);
                }
            }
        } finally {
            comboReady = saved;
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        updateConfigurations();
    }
    
    private void updateConfigurations() {
        boolean saved = comboReady;
        
        GradleExecConfiguration toSelect = null;
        try {
            comboReady = false;
            // resync the configuration combo with the Snapshot, just in case when some configuration was defined
            // in the other panel.
            GradleExecConfiguration cur = (GradleExecConfiguration)configModel.getSelectedItem();
            GradleExecConfiguration act = configSnapshot.getActiveConfiguration();
            configModel.removeAllElements();;
            for (GradleExecConfiguration c : configSnapshot.getConfigurations()) {
                configModel.addElement(c);
                if (cur != null) {
                    if (c.equals(cur)) {
                        toSelect = c;
                     }
                } else if (c.equals(act)) {
                    toSelect = act;
                }
            }
        } finally {
            comboReady = saved;
        }
        if (toSelect == null) {
            toSelect = configSnapshot.getActiveConfiguration();
        }
        configModel.setSelectedItem(toSelect);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        lbConfiguration = new javax.swing.JLabel();
        cbConfiguration = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        lbActions = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lsActions = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        cbAdd = new javax.swing.JComboBox<>();
        pnDetailsPanel = new javax.swing.JPanel();
        pnDetails = new javax.swing.JPanel();
        lbName = new javax.swing.JLabel();
        lbLabel = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        tfLabel = new javax.swing.JTextField();
        lbArgs = new javax.swing.JLabel();
        btRemove = new javax.swing.JButton();
        lbReloadRule = new javax.swing.JLabel();
        cbReloadRule = new javax.swing.JComboBox<>();
        cbRepeatable = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        taArgs = new javax.swing.JEditorPane();
        lbReloadHints = new javax.swing.JLabel();
        lbNoAction = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(lbTitle, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbTitle.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbConfiguration, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbConfiguration.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbActions, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbActions.text")); // NOI18N

        lsActions.setModel(customActionsModel);
        lsActions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lsActions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lsActionsValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lsActions);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.jLabel1.text")); // NOI18N

        cbAdd.setModel(availableActionsModel);
        cbAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAddActionPerformed(evt);
            }
        });

        pnDetailsPanel.setLayout(new java.awt.CardLayout());

        pnDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        org.openide.awt.Mnemonics.setLocalizedText(lbName, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbLabel, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbLabel.text")); // NOI18N

        tfName.setEditable(false);
        tfName.setEnabled(false);

        tfLabel.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(lbArgs, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbArgs.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btRemove, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.btRemove.text")); // NOI18N
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lbReloadRule, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbReloadRule.text")); // NOI18N

        cbReloadRule.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NEVER", "DEFAULT", "ALWAYS", "ALWAYS_ONLINE" }));
        cbReloadRule.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbReloadRuleItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cbRepeatable, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.cbRepeatable.text")); // NOI18N
        cbRepeatable.setToolTipText(org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.cbRepeatable.toolTipText")); // NOI18N
        cbRepeatable.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbRepeatableStateChanged(evt);
            }
        });

        taArgs.setContentType("text/x-gradle-cli"); // NOI18N
        jScrollPane3.setViewportView(taArgs);

        lbReloadHints.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lbReloadHints.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(lbReloadHints, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbReloadHints.text")); // NOI18N
        lbReloadHints.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbReloadHints.setEnabled(false);

        javax.swing.GroupLayout pnDetailsLayout = new javax.swing.GroupLayout(pnDetails);
        pnDetails.setLayout(pnDetailsLayout);
        pnDetailsLayout.setHorizontalGroup(
            pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDetailsLayout.createSequentialGroup()
                .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDetailsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btRemove))
                    .addGroup(pnDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnDetailsLayout.createSequentialGroup()
                                .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbName)
                                    .addComponent(lbLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfLabel)
                                    .addComponent(tfName)))
                            .addGroup(pnDetailsLayout.createSequentialGroup()
                                .addComponent(lbReloadRule)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbReloadRule, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbRepeatable, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDetailsLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane3))
                            .addGroup(pnDetailsLayout.createSequentialGroup()
                                .addComponent(lbArgs)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(lbReloadHints, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnDetailsLayout.setVerticalGroup(
            pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbName)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLabel)
                    .addComponent(tfLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbArgs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbReloadRule)
                    .addComponent(cbReloadRule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbRepeatable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbReloadHints, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRemove)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnDetailsPanel.add(pnDetails, "details");

        lbNoAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(lbNoAction, org.openide.util.NbBundle.getMessage(BuildActionsCustomizer.class, "BuildActionsCustomizer.lbNoAction.text")); // NOI18N
        pnDetailsPanel.add(lbNoAction, "empty");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbActions))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbAdd, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbConfiguration)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbConfiguration, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbConfiguration, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbConfiguration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbActions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lsActionsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lsActionsValueChanged
        CardLayout cardLayout = (CardLayout) pnDetailsPanel.getLayout();
        ActionMapping mapping = getSelectedMapping();
        autoApply = false;
        if (mapping != null) {
            cardLayout.show(pnDetailsPanel, CARD_DETAILS);

            tfName.setText(mapping.getName());
            tfLabel.setText(mapping.getDisplayName());
            tfLabel.setEnabled(mapping.getName().startsWith(ActionMapping.CUSTOM_PREFIX));
            taArgs.setText(mapping.getArgs());
            cbReloadRule.setSelectedItem(mapping.getReloadRule().name());
            cbRepeatable.setSelected(mapping.isRepeatable());
            autoApply = true;
        } else {
            cardLayout.show(pnDetailsPanel, CARD_NOSELECT);
        }
    }//GEN-LAST:event_lsActionsValueChanged

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        CustomActionMapping mapping = getSelectedMapping();
        customActionsModel.removeElement(mapping);
        actionRegistry.unregisterCustomAction(mapping.getName());
        String action = mapping.getName();
        if (!action.startsWith(ActionMapping.CUSTOM_PREFIX)) {
            availableActionsModel.addElement(action);
        }
    }//GEN-LAST:event_btRemoveActionPerformed

    private void apply() {
        if (autoApply) {
            CustomActionMapping mapping = getSelectedMapping();
            if (mapping.getName().startsWith(ActionMapping.CUSTOM_PREFIX)) {
                mapping.setDisplayName(tfLabel.getText());
            }
            mapping.setArgs(taArgs.getText());
            mapping.setReloadRule(ActionMapping.ReloadRule.valueOf(cbReloadRule.getSelectedItem().toString()));
            mapping.setRepeatable(cbRepeatable.isSelected());

            actionRegistry.registerCustomAction(mapping);
            lsActions.repaint();
        }
    }

    private void cbRepeatableStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cbRepeatableStateChanged
        apply();
    }//GEN-LAST:event_cbRepeatableStateChanged

    private void cbReloadRuleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbReloadRuleItemStateChanged
        apply();
    }//GEN-LAST:event_cbReloadRuleItemStateChanged

    private void cbAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAddActionPerformed
        if (!comboReady) return;
        String action = availableActionsModel.getElementAt(cbAdd.getSelectedIndex());
        if (action != CUSTOM_ACTION) {
            availableActionsModel.removeElement(action);
        } else {
            action = actionRegistry.findNewCustonActionId();
        }
        ProjectActionMappingProvider mappingProvider = project.getLookup().lookup(ProjectActionMappingProvider.class);
        ActionMapping defaultMapping = mappingProvider.findMapping(action);
        CustomActionMapping mapping = defaultMapping != null ? new CustomActionMapping(defaultMapping) : new CustomActionMapping(action);
        customActionsModel.addElement(mapping);
        actionRegistry.registerCustomAction(mapping);
        lsActions.setSelectedIndex(customActionsModel.indexOf(mapping));
        cbAdd.setSelectedIndex(0);
    }//GEN-LAST:event_cbAddActionPerformed

    private CustomActionMapping getSelectedMapping() {
        int index = lsActions.getSelectedIndex();
        return index >= 0 ? customActionsModel.elementAt(index) : null;
    }

    void save() {
        actionRegistry.saveAndReportErrors();
    }

    static class MyListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof CustomActionMapping) {
                CustomActionMapping mapping = (CustomActionMapping) value;
                String displayName = mapping.getDisplayName();
                if (mapping.getArgs().contains("${input:")) {
                    displayName = displayName + "...";
                }
                label.setText(mapping.isChanged() ? "<html><b>" + displayName + "</b>" : displayName);
            }
            return label;
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btRemove;
    private javax.swing.JComboBox<String> cbAdd;
    private javax.swing.JComboBox<GradleExecConfiguration> cbConfiguration;
    private javax.swing.JComboBox<String> cbReloadRule;
    private javax.swing.JCheckBox cbRepeatable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbActions;
    private javax.swing.JLabel lbArgs;
    private javax.swing.JLabel lbConfiguration;
    private javax.swing.JLabel lbLabel;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbNoAction;
    private javax.swing.JLabel lbReloadHints;
    private javax.swing.JLabel lbReloadRule;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JList<CustomActionMapping> lsActions;
    private javax.swing.JPanel pnDetails;
    private javax.swing.JPanel pnDetailsPanel;
    private javax.swing.JEditorPane taArgs;
    private javax.swing.JTextField tfLabel;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables

    private static Map<String, ConfigData>  actionConfigMap = new HashMap<>();
    
    static class ConfigData {
        final DefaultListModel<CustomActionMapping> customActionsModel = new DefaultListModel<>();
        final DefaultComboBoxModel<String> availableActionsModel = new DefaultComboBoxModel<>();
    }
}
