package com.xlh.plugin.data;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.xlh.plugin.data.vo.FastMVCConfigVO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 持久化存储类
 *
 * @author xingluheng
 * @date 2023/07/19 20:43
 **/
@State(name = "DataSetting", storages = @Storage("dataSetting.xml"))
public class DataSetting implements PersistentStateComponent<DataState> {

    private DataState state = new DataState();

    public static DataSetting getInstance(Project project) {
        return project.getService(DataSetting.class);
    }

    @Nullable
    @Override
    public DataState getState() {
        return this.state;
    }

    @Override
    public void loadState(@NotNull DataState state) {
        this.state = state;
    }

    public FastMVCConfigVO getFastMVCConfigVO() {
        return state.getFastMVCConfigVO();
    }
}
