var metrics = {
    sectionName: "Metrics",
    group: "metric",
    items: [
        {
            name: "Euclidean",
            code: "EUCLIDEAN",
            description: "Euclidean metric is the 'ordinary' distance between two points in Euclidean space"
        },
        {
            name: "Cosine",
            code: "COSINE",
            description: "Cosine similarity is a measure of similarity between two vectors"
        },
        {
            name: "Taxicab",
            code: "L1D",
            description: "Taxicab geometry is a form of geometry in which the usual distance function of metric"
        }
    ]
};

var trainingSettings = {
    sectionName: "Settings [Choose Training Images]",
    group: "training",
    items: [
        {
            name: "Use ALL images",
            code: "ALL",
            description: "Uses all exist images with faces"
        },
        {
            name: "Use FIRST images",
            code: "FIRST",
            description: "Uses first n exist images for each class",
            defaultValue: 5,
            input: true
        },
        {
            name: "Use RANDOM images",
            code: "RANDOM",
            description: "Uses n randomly selected images for each class",
            defaultValue: 5,
            input: true
        }
    ]
};

var knnSettings = {
    sectionName: "Settings [KNN]",
    group: "knn",
    items: [
        {
            name: "Use DEFAULT number",
            code: "DEFAULT",
            description: "Uses DEFAULT number of Nearest Neighbor components from .properties file"
        },
        {
            name: "Use CUSTOM number",
            code: "CUSTOM",
            description: "Uses YOUR number of Nearest Neighbor components",
            defaultValue: 2,
            input: true
        }
    ]
};

var pcSettings = {
    sectionName: "Settings [Principal Components]",
    group: "components",
    items: [
        {
            name: "Use DEFAULT number",
            code: "DEFAULT",
            description: "Uses DEFAULT number of Principal Components from .properties file"
        },
        {
            name: "Use CUSTOM number",
            code: "CUSTOM",
            description: "Uses YOUR number of Principal Components",
            defaultValue: 40,
            input: true
        }
    ]
};

var stringDistance = {
    sectionName: "Settings [Hash String Distance]",
    group: "stringDistance",
    items: [
        {
            name: "Hamming Distance",
            code: "HAMMING",
            description: "Hamming distance between two strings of equal length is the number of positions at which the corresponding symbols are different"
        },
        {
            name: "Jaro Winkler Distance",
            code: "JARO_WINKLER",
            description: "Jaro–Winkler distance is a measure of similarity between two strings"
        },
        {
            name: "Levenstein Distance",
            code: "LEVENSTEIN",
            description: "Levenshtein distance is a string metric for measuring the difference between two sequences"
        }
    ]
};

var algorithms = {
    sectionName: "Algorithms",
    group: "algorithm",
    items: [
        {
            name: "PCA",
            code: "PCA",
            description: "Principal Component Analysis",
            settings: [metrics, trainingSettings, knnSettings, pcSettings]
        },
        {
            name: "LDA",
            code: "LDA",
            description: "Linear Discriminant Analysis",
            settings: [metrics, trainingSettings, knnSettings, pcSettings]
        },
        {
            name: "LPP",
            code: "LPP",
            description: "Locality Preserving Projections",
            settings: [metrics, trainingSettings, knnSettings, pcSettings]
        },
        {
            name: "NBC",
            code: "NBC",
            description: "Naive Bayes Classifier",
            settings: [trainingSettings]
        },
        {
            name: "Average Hash",
            code: "AHASH",
            description: "Perceptual Average Hash Algorithm",
            settings: [trainingSettings, stringDistance]
        },
        {
            name: "DCT Hash",
            code: "DCT_HASH",
            description: "Perceptual Hash Algorithm with Discrete Cosine Transform",
            settings: [trainingSettings, stringDistance]
        }
    ]
};