package com.xlh.plugin.data;

import com.xlh.plugin.data.vo.FastMVCConfigVO;

/**
 * @author xingluheng
 */
public class DataState {

    private FastMVCConfigVO fastMVCConfigVO = new FastMVCConfigVO();

    public FastMVCConfigVO getFastMVCConfigVO() {
        return fastMVCConfigVO;
    }

    public void setFastMVCConfigVO(FastMVCConfigVO fastMVCConfigVO) {
        this.fastMVCConfigVO = fastMVCConfigVO;
    }
}
