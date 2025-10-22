import { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, Clock, Users, Award } from 'lucide-react';
import { BANNER_SLIDES } from '../../config/homePageConfig';

export const HeroSection = () => {
  const [currentSlide, setCurrentSlide] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % BANNER_SLIDES.length);
    }, 5000);
    return () => clearInterval(timer);
  }, []);

  const goToPrevious = () => {
    setCurrentSlide((prev) => (prev - 1 + BANNER_SLIDES.length) % BANNER_SLIDES.length);
  };

  const goToNext = () => {
    setCurrentSlide((prev) => (prev + 1) % BANNER_SLIDES.length);
  };

  const currentBanner = BANNER_SLIDES[currentSlide];

  // Simple solid colors - guaranteed visible
  const backgrounds = [
    '#1e40af', // Solid blue
    '#059669', // Solid green
    '#dc2626', // Solid red
    '#7c3aed', // Solid purple
    '#ea580c', // Solid orange
  ];

  return (
    <section 
      style={{ 
        height: '350px',
        backgroundColor: backgrounds[currentSlide],
        position: 'relative',
        overflow: 'hidden'
      }}
    >
      {/* Content */}
      <div style={{
        height: '100%',
        maxWidth: '1200px',
        margin: '0 auto',
        padding: '0 1rem',
        display: 'flex',
        alignItems: 'center'
      }}>
        <div style={{ maxWidth: '800px' }}>
          {/* Special Badge */}
          {currentSlide === 0 && (
            <div style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: '0.5rem',
              backgroundColor: '#ef4444',
              color: 'white',
              padding: '0.75rem 1.5rem',
              borderRadius: '9999px',
              fontSize: '0.875rem',
              fontWeight: 'bold',
              marginBottom: '1.5rem'
            }}>
              <span>🎉</span>
              <span>LIMITED TIME OFFER!</span>
            </div>
          )}

          {/* Title */}
          <h1 style={{
            fontSize: '3rem',
            fontWeight: 'bold',
            marginBottom: '1.5rem',
            color: 'white',
            lineHeight: '1.2'
          }}>
            {currentBanner.title}
          </h1>
          
          {/* Subtitle */}
          <p style={{
            fontSize: '1.25rem',
            marginBottom: '2rem',
            color: 'white',
            lineHeight: '1.75',
            maxWidth: '700px'
          }}>
            {currentBanner.subtitle}
          </p>

          {/* CTA Buttons */}
          <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', marginBottom: '2rem' }}>
            <a
              href={currentBanner.buttonLink}
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: '0.75rem',
                backgroundColor: 'white',
                color: '#1f2937',
                padding: '1rem 2rem',
                borderRadius: '0.5rem',
                fontWeight: 'bold',
                fontSize: '1.125rem',
                textDecoration: 'none'
              }}
            >
              {currentBanner.buttonText}
              <span>→</span>
            </a>
            
            <a
              href="/courses"
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: '0.5rem',
                border: '2px solid white',
                color: 'white',
                padding: '0.75rem 1.5rem',
                borderRadius: '0.5rem',
                fontWeight: '600',
                textDecoration: 'none'
              }}
            >
              View All Courses
            </a>
          </div>

          {/* Trust Indicators */}
          <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', fontSize: '0.875rem', color: 'white' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Users className="h-5 w-5" />
              <span>50,000+ Students</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Award className="h-5 w-5" />
              <span>95% Success Rate</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Clock className="h-5 w-5" />
              <span>24/7 Support</span>
            </div>
          </div>
        </div>
      </div>

      {/* Navigation Arrows */}
      <button
        onClick={goToPrevious}
        style={{
          position: 'absolute',
          left: '1rem',
          top: '50%',
          transform: 'translateY(-50%)',
          backgroundColor: 'rgba(255,255,255,0.3)',
          padding: '0.75rem',
          borderRadius: '9999px',
          border: 'none',
          cursor: 'pointer'
        }}
      >
        <ChevronLeft className="h-6 w-6 text-white" />
      </button>
      <button
        onClick={goToNext}
        style={{
          position: 'absolute',
          right: '1rem',
          top: '50%',
          transform: 'translateY(-50%)',
          backgroundColor: 'rgba(255,255,255,0.3)',
          padding: '0.75rem',
          borderRadius: '9999px',
          border: 'none',
          cursor: 'pointer'
        }}
      >
        <ChevronRight className="h-6 w-6 text-white" />
      </button>

      {/* Dots Indicator */}
      <div style={{
        position: 'absolute',
        bottom: '1.5rem',
        left: '50%',
        transform: 'translateX(-50%)',
        display: 'flex',
        gap: '0.75rem'
      }}>
        {BANNER_SLIDES.map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentSlide(index)}
            style={{
              height: '12px',
              width: index === currentSlide ? '40px' : '12px',
              backgroundColor: index === currentSlide ? 'white' : 'rgba(255,255,255,0.5)',
              borderRadius: '9999px',
              border: 'none',
              cursor: 'pointer'
            }}
          />
        ))}
      </div>

      {/* Countdown Timer */}
      {currentSlide === 0 && (
        <div style={{
          position: 'absolute',
          bottom: '1.5rem',
          right: '1.5rem',
          backgroundColor: '#dc2626',
          color: 'white',
          padding: '0.5rem 1rem',
          borderRadius: '0.5rem',
          fontSize: '0.875rem',
          fontWeight: 'bold'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Clock className="h-4 w-4" />
            <span>Offer ends in: 4 days!</span>
          </div>
        </div>
      )}
    </section>
  );
};
