package com.anamnesys.util;

import com.anamnesys.controller.dto.SegmentRequest;
import com.anamnesys.controller.dto.SegmentResponse;
import com.anamnesys.repository.model.SegmentModel;

public class SegmentMapper {

    public static SegmentResponse getSegment(SegmentModel segment) {
        SegmentResponse segmentResponse = new SegmentResponse();
        segmentResponse.setDescription(segment.getDescription());
        segmentResponse.setId(segment.getId());
        segmentResponse.setName(segment.getName());
        return segmentResponse;
    }

    public static SegmentModel getSegment(SegmentRequest segment) {
        SegmentModel model = new SegmentModel();
        model.setDescription(segment.getDescription());
        model.setName(segment.getName());
        return model;
    }
}
