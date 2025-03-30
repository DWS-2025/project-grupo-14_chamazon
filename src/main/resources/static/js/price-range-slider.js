document.addEventListener('DOMContentLoaded', function() {
    const priceSlider = document.getElementById('price-range-slider');
    const minPriceInput = document.getElementById('minPrice');
    const maxPriceInput = document.getElementById('maxPrice');
    const priceRangeText = document.getElementById('price-range-text');


    const minPriceInitial = minPriceInput.value ? parseFloat(minPriceInput.value) : 0;
    const maxPriceInitial = maxPriceInput.value ? parseFloat(maxPriceInput.value) : 1000;


    noUiSlider.create(priceSlider, {
        start: [minPriceInitial, maxPriceInitial],
        connect: true,
        step: 1,
        range: {
            'min': 0,
            'max': 1000
        },
        format: {
            to: function(value) {
                return Math.round(value);
            },
            from: function(value) {
                return Number(value);
            }
        }
    });


    priceSlider.noUiSlider.on('update', function(values, handle) {
        const minValue = values[0];
        const maxValue = values[1];

        minPriceInput.value = minValue;
        maxPriceInput.value = maxValue;


        priceRangeText.innerHTML = `${minValue}&euro; - ${maxValue}&euro;`;
    });


    minPriceInput.addEventListener('change', function() {
        priceSlider.noUiSlider.set([this.value, null]);
    });

    maxPriceInput.addEventListener('change', function() {
        priceSlider.noUiSlider.set([null, this.value]);
    });
});