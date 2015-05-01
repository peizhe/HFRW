package com.kol.recognition.general;

import com.kol.recognition.components.beans.AnalysisSettingsPlaceholder;
import com.kol.recognition.components.recognition.LDA;
import com.kol.recognition.components.recognition.LPP;
import com.kol.recognition.components.recognition.PCA;
import com.kol.recognition.nbc.NBCRecognizer;
import com.kol.recognition.nbc.NBCSettingsPlaceholder;
import com.kol.recognition.perceptualHash.bean.HashSettingsPlaceholder;
import com.kol.recognition.perceptualHash.hash.AverageHash;
import com.kol.recognition.perceptualHash.hash.DCTHash;
import com.kol.recognition.perceptualHash.hash.HashRecognizer;

public enum RecognitionAlgorithm {
    PCA(AlgorithmType.COMPONENT) {
        @Override
        public <T extends SettingsPlaceholder> Algorithm get(final T placeholder) {
            final AnalysisSettingsPlaceholder settings = (AnalysisSettingsPlaceholder) placeholder;
            return new PCA(settings.getData(), settings.getComponents(), settings.getVecLength(), settings.getTrain(), settings.getSettings());
        }
    },
    LDA(AlgorithmType.COMPONENT) {
        @Override
        public <T extends SettingsPlaceholder> Algorithm get(final T placeholder) {
            final AnalysisSettingsPlaceholder settings = (AnalysisSettingsPlaceholder) placeholder;
            return new LDA(settings.getData(), settings.getComponents(), settings.getVecLength(), settings.getTrain(), settings.getSettings());
        }
    },
    NBC(AlgorithmType.CLASSIFY) {
        @Override
        public <T extends SettingsPlaceholder> Algorithm get(final T placeholder) {
            final NBCSettingsPlaceholder settings = (NBCSettingsPlaceholder) placeholder;
            return new NBCRecognizer(settings.getData(), settings.getTrain(), settings.getWidth(), settings.getHeight());
        }
    },
    LPP(AlgorithmType.COMPONENT) {
        @Override
        public <T extends SettingsPlaceholder> Algorithm get(final T placeholder) {
            final AnalysisSettingsPlaceholder settings = (AnalysisSettingsPlaceholder) placeholder;
            return new LPP(settings.getData(), settings.getComponents(), settings.getVecLength(), settings.getTrain(), settings.getSettings());
        }
    },
    DCT_HASH(AlgorithmType.HASH) {
        @Override
        public <T extends SettingsPlaceholder> Algorithm get(T placeholder) {
            final HashSettingsPlaceholder settings = (HashSettingsPlaceholder) placeholder;
            return new HashRecognizer(new DCTHash(settings.getWidth(), settings.getHeight()), settings.getDistance(), settings.getData(), settings.getTrain());
        }
    },
    AHASH(AlgorithmType.HASH) {
        @Override
        public <T extends SettingsPlaceholder> Algorithm get(T placeholder) {
            final HashSettingsPlaceholder settings = (HashSettingsPlaceholder) placeholder;
            return new HashRecognizer(new AverageHash(settings.getWidth(), settings.getHeight()), settings.getDistance(), settings.getData(), settings.getTrain());
        }
    };

    RecognitionAlgorithm(AlgorithmType type) {
        this.type = type;
    }

    private AlgorithmType type;

    public AlgorithmType getType() {
        return type;
    }

    public void setType(AlgorithmType type) {
        this.type = type;
    }

    public abstract <T extends SettingsPlaceholder> Algorithm get(final T placeholder);

    public enum AlgorithmType {
        COMPONENT, HASH, CLASSIFY
    }
}