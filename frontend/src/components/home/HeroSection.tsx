import { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, Clock, Users, Award } from 'lucide-react';
import { BANNER_SLIDES } from '../../config/homePageConfig';

export const HeroSection = () => {
  const [currentSlide, setCurrentSlide] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % BANNER_SLIDES.length);
    }, 4000); // Slower transition for reading offers
    return () => clearInterval(timer);
  }, []);

  const goToPrevious = () => {
    setCurrentSlide((prev) => (prev - 1 + BANNER_SLIDES.length) % BANNER_SLIDES.length);
  };

  const goToNext = () => {
    setCurrentSlide((prev) => (prev + 1) % BANNER_SLIDES.length);
  };

  const currentBanner = BANNER_SLIDES[currentSlide];

  return (
    <section 
      className="relative overflow-hidden" 
      style={{ height: '400px' }}
    >
      {/* Background Image with Enhanced Overlay */}
      <div 
        className="absolute inset-0 bg-cover bg-center transition-all duration-1000"
        style={{ backgroundImage: `url(${currentBanner.image})` }}
      >
        <div className={`absolute inset-0 bg-gradient-to-r ${currentBanner.gradient}`} />
        {/* Additional overlay for better text readability */}
        <div className="absolute inset-0 bg-black/20" />
      </div>

      {/* Professional Content Layout */}
      <div className="relative h-full container mx-auto px-4 flex items-center">
        <div className="max-w-3xl text-white">
          {/* Promotional Badge */}
          {currentSlide === 0 && (
            <div className="inline-flex items-center gap-2 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium mb-4">
              <span className="animate-pulse">🎉</span>
              <span>Limited Time Offer - Ends Soon!</span>
            </div>
          )}

          {/* Main Title with Animation */}
          <h1 className="text-3xl md:text-5xl font-bold mb-4 leading-tight">
            {currentBanner.title}
          </h1>
          
          {/* Subtitle with Professional Styling */}
          <p className="text-lg md:text-xl mb-6 opacity-95 leading-relaxed max-w-2xl">
            {currentBanner.subtitle}
          </p>

          {/* Enhanced CTA Section */}
          <div className="flex flex-col sm:flex-row items-start sm:items-center gap-4 mb-6">
            <a
              href={currentBanner.buttonLink}
              className="inline-flex items-center gap-2 bg-white text-gray-900 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-1"
            >
              {currentBanner.buttonText}
              <span className="ml-2">→</span>
            </a>
            
            {/* Secondary CTA for some slides */}
            {(currentSlide === 0 || currentSlide === 1) && (
              <a
                href="/courses"
                className="inline-flex items-center gap-2 border-2 border-white text-white px-6 py-3 rounded-lg font-medium hover:bg-white hover:text-gray-900 transition-all duration-200"
              >
                View All Courses
              </a>
            )}
          </div>

          {/* Trust Indicators */}
          <div className="flex items-center gap-6 text-sm opacity-90">
            <div className="flex items-center gap-2">
              <Users className="h-4 w-4" />
              <span>50,000+ Students</span>
            </div>
            <div className="flex items-center gap-2">
              <Award className="h-4 w-4" />
              <span>95% Success Rate</span>
            </div>
            <div className="flex items-center gap-2">
              <Clock className="h-4 w-4" />
              <span>24/7 Support</span>
            </div>
          </div>
        </div>
      </div>

      {/* Professional Navigation Arrows */}
      <button
        onClick={goToPrevious}
        className="absolute left-4 top-1/2 -translate-y-1/2 bg-white/10 hover:bg-white/20 p-3 rounded-full backdrop-blur-sm transition-all duration-200 group"
      >
        <ChevronLeft className="h-6 w-6 text-white group-hover:scale-110 transition-transform" />
      </button>
      <button
        onClick={goToNext}
        className="absolute right-4 top-1/2 -translate-y-1/2 bg-white/10 hover:bg-white/20 p-3 rounded-full backdrop-blur-sm transition-all duration-200 group"
      >
        <ChevronRight className="h-6 w-6 text-white group-hover:scale-110 transition-transform" />
      </button>

      {/* Enhanced Dots Indicator */}
      <div className="absolute bottom-6 left-1/2 -translate-x-1/2 flex gap-3">
        {BANNER_SLIDES.map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentSlide(index)}
            className={`h-3 rounded-full transition-all duration-300 ${
              index === currentSlide 
                ? 'w-8 bg-white shadow-lg' 
                : 'w-3 bg-white/50 hover:bg-white/75'
            }`}
          />
        ))}
      </div>

      {/* Promotional Timer (for special offers) */}
      {currentSlide === 0 && (
        <div className="absolute bottom-6 right-6 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-lg text-white text-sm">
          <div className="flex items-center gap-2">
            <Clock className="h-4 w-4 text-red-300" />
            <span>Offer ends in: <strong>5 days</strong></span>
          </div>
        </div>
      )}
    </section>
  );
};
