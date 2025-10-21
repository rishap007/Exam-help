// ==========================================
// MAIN HOMEPAGE COMPONENT
// Save as: src/pages/home/HomePage.tsx
// ==========================================

import { useState } from 'react';
import { BookOpen, Menu, X, ChevronDown } from 'lucide-react';

import { HeroSection } from '../../components/home/HeroSection';
import { TeacherHighlight } from '../../components/home/TeacherHighlight';
import { CategoryGrid } from '../../components/home/CategoryGrid';
import { AcademicExcellence } from '../../components/home/AcademicExcellence';
import { Footer } from '../../components/home/Footer';
import { SpecialOffers } from '../../components/home/SpecialOffer';

export const HomePage = () => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  return (
    <div className="min-h-screen bg-background">
      {/* Navbar */}
      {/* Professional Navbar */}
      {/* Clean White Navbar - PW Style */}
      <nav className="sticky top-0 z-50 bg-white border-b border-gray-100 shadow-sm">
        <div className="container mx-auto px-4">
          <div className="flex h-16 items-center justify-between"> {/* Fixed height, removed extra padding */}
            {/* Logo */}
            <a href="/" className="flex items-center gap-2">
              <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary text-white">
                <BookOpen className="h-4 w-4" />
              </div>
              <span className="text-xl font-bold text-gray-900">
                Classtopper
              </span>
            </a>

            {/* Desktop Navigation */}
            <div className="hidden items-center gap-8 lg:flex">
              {/* Courses Dropdown */}
              <div className="relative group">
                <button className="flex items-center gap-1 text-sm font-medium text-gray-700 hover:text-primary transition-colors py-2">
                  Courses
                  <ChevronDown className="h-4 w-4 group-hover:rotate-180 transition-transform duration-200" />
                </button>
                <div className="absolute top-full left-0 mt-1 w-72 bg-white border border-gray-100 rounded-xl shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 z-50">
                  <div className="p-6 space-y-4">
                    <a href="/courses/jee" className="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group/item">
                      <div className="h-10 w-10 rounded-lg bg-blue-50 flex items-center justify-center text-primary font-bold text-sm">
                        JEE
                      </div>
                      <div>
                        <div className="font-semibold text-gray-900 group-hover/item:text-primary">JEE Main & Advanced</div>
                        <div className="text-xs text-gray-500 mt-1">Complete Engineering Entrance Prep</div>
                      </div>
                    </a>
                    <a href="/courses/neet" className="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group/item">
                      <div className="h-10 w-10 rounded-lg bg-green-50 flex items-center justify-center text-green-600 font-bold text-sm">
                        NEET
                      </div>
                      <div>
                        <div className="font-semibold text-gray-900 group-hover/item:text-green-600">NEET UG</div>
                        <div className="text-xs text-gray-500 mt-1">Medical Entrance Examination</div>
                      </div>
                    </a>
                    <a href="/courses/boards" className="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group/item">
                      <div className="h-10 w-10 rounded-lg bg-purple-50 flex items-center justify-center text-purple-600 font-bold text-sm">
                        12th
                      </div>
                      <div>
                        <div className="font-semibold text-gray-900 group-hover/item:text-purple-600">Class 6-12 Boards</div>
                        <div className="text-xs text-gray-500 mt-1">CBSE, ICSE & State Boards</div>
                      </div>
                    </a>
                    <a href="/courses/foundation" className="flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors group/item">
                      <div className="h-10 w-10 rounded-lg bg-orange-50 flex items-center justify-center text-orange-600 font-bold text-sm">
                        FN
                      </div>
                      <div>
                        <div className="font-semibold text-gray-900 group-hover/item:text-orange-600">Foundation</div>
                        <div className="text-xs text-gray-500 mt-1">Strong Conceptual Foundation</div>
                      </div>
                    </a>
                  </div>
                </div>
              </div>

              {/* Regular Nav Items */}
              <a href="/test-series" className="text-sm font-medium text-gray-700 hover:text-primary transition-colors">
                Test Series
              </a>
              <a href="/bookstore" className="text-sm font-medium text-gray-700 hover:text-primary transition-colors">
                CT Bookstore
              </a>
              <a href="/live-classes" className="text-sm font-medium text-gray-700 hover:text-primary transition-colors">
                Live Classes
              </a>
              <a href="/results" className="text-sm font-medium text-gray-700 hover:text-primary transition-colors">
                Results
              </a>

              {/* More Dropdown */}
              <div className="relative group">
                <button className="flex items-center gap-1 text-sm font-medium text-gray-700 hover:text-primary transition-colors py-2">
                  More
                  <ChevronDown className="h-4 w-4 group-hover:rotate-180 transition-transform duration-200" />
                </button>
                <div className="absolute top-full right-0 mt-1 w-56 bg-white border border-gray-100 rounded-xl shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 z-50">
                  <div className="p-4 space-y-2">
                    <a href="/free-content" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-green-500">🎯</span>
                      <span className="text-sm font-medium text-gray-700">Free Content</span>
                    </a>
                    <a href="/doubt-solving" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-blue-500">❓</span>
                      <span className="text-sm font-medium text-gray-700">Ask Doubts</span>
                    </a>
                    <a href="/scholarships" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-yellow-500">🏆</span>
                      <span className="text-sm font-medium text-gray-700">Scholarships</span>
                    </a>
                    <hr className="my-2 border-gray-100" />
                    <a href="/careers" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-purple-500">💼</span>
                      <span className="text-sm font-medium text-gray-700">Careers</span>
                    </a>
                    <a href="/about" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-gray-500">ℹ️</span>
                      <span className="text-sm font-medium text-gray-700">About Us</span>
                    </a>
                    <a href="/contact" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-red-500">📞</span>
                      <span className="text-sm font-medium text-gray-700">Contact</span>
                    </a>
                  </div>
                </div>
              </div>
            </div>

            {/* Clean CTA Section */}
            <div className="hidden items-center gap-3 lg:flex">
              <a
                href="tel:18001234567"
                className="flex items-center gap-2 text-sm font-medium text-gray-600 hover:text-primary transition-colors"
              >
                <span className="text-green-500">📞</span>
                1800-123-4567
              </a>
              <a
                href="/login"
                className="inline-flex items-center justify-center px-4 py-2 text-sm font-medium text-gray-700 border border-gray-200 rounded-lg hover:bg-gray-50 transition-all duration-200"
              >
                Sign In
              </a>
              <a
                href="/register"
                className="inline-flex items-center justify-center px-6 py-2 text-sm font-medium text-white bg-primary rounded-lg hover:bg-primary/90 transition-all duration-200"
              >
                Start Free Trial
              </a>
            </div>

            {/* Mobile Menu Button */}
            <button
              className="flex items-center justify-center p-2 rounded-lg lg:hidden hover:bg-gray-50 transition-colors"
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            >
              {mobileMenuOpen ? (
                <X className="h-6 w-6 text-gray-700" />
              ) : (
                <Menu className="h-6 w-6 text-gray-700" />
              )}
            </button>
          </div>

          {/* Clean Mobile Menu */}
          {mobileMenuOpen && (
            <div className="border-t border-gray-100 bg-white lg:hidden">
              <div className="px-4 py-6 space-y-6">
                {/* Course Categories */}
                <div>
                  <div className="font-semibold text-gray-900 mb-3">COURSES</div>
                  <div className="space-y-2">
                    <a href="/courses/jee" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-blue-50 flex items-center justify-center text-primary text-xs font-bold">JEE</div>
                      <span className="text-sm font-medium text-gray-700">JEE Main & Advanced</span>
                    </a>
                    <a href="/courses/neet" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-green-50 flex items-center justify-center text-green-600 text-xs font-bold">NEET</div>
                      <span className="text-sm font-medium text-gray-700">NEET UG</span>
                    </a>
                    <a href="/courses/boards" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-purple-50 flex items-center justify-center text-purple-600 text-xs font-bold">12th</div>
                      <span className="text-sm font-medium text-gray-700">Class 6-12 Boards</span>
                    </a>
                    <a href="/courses/foundation" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-orange-50 flex items-center justify-center text-orange-600 text-xs font-bold">FN</div>
                      <span className="text-sm font-medium text-gray-700">Foundation</span>
                    </a>
                  </div>
                </div>

                {/* Services */}
                <div>
                  <div className="font-semibold text-gray-900 mb-3">SERVICES</div>
                  <div className="space-y-2">
                    <a href="/test-series" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Test Series
                    </a>
                    <a href="/bookstore" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      CT Bookstore
                    </a>
                    <a href="/live-classes" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Live Classes
                    </a>
                    <a href="/results" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Results
                    </a>
                    <a href="/free-content" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Free Content
                    </a>
                    <a href="/doubt-solving" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Ask Doubts
                    </a>
                  </div>
                </div>

                {/* Auth Buttons */}
                <div className="space-y-3 pt-4 border-t border-gray-100">
                  <a
                    href="/login"
                    className="block w-full text-center px-4 py-3 text-sm font-medium text-gray-700 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    Sign In
                  </a>
                  <a
                    href="/register"
                    className="block w-full text-center px-4 py-3 text-sm font-medium text-white bg-primary rounded-lg hover:bg-primary/90 transition-colors"
                  >
                    Start Free Trial
                  </a>
                  <a href="tel:18001234567" className="block text-center text-sm text-gray-600 pt-2">
                    📞 1800-123-4567
                  </a>
                </div>
              </div>
            </div>
          )}
        </div>
      </nav>



      {/* Hero Section */}
      <HeroSection />

      <SpecialOffers />

      {/* Teacher Highlight */}
      <TeacherHighlight />

      {/* Exam Categories */}
      <CategoryGrid />

      {/* Academic Excellence */}
      <AcademicExcellence />

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default HomePage;
