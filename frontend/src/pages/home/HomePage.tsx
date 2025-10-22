// ==========================================
// FINAL PROFESSIONAL HOMEPAGE COMPONENT
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
    <div className="min-h-screen bg-white">
      {/* Professional Clean White Navbar */}
      <nav className="sticky top-0 z-50 bg-white border-b border-gray-200 shadow-sm">
        <div className="container mx-auto px-4">
          <div className="flex h-16 items-center justify-between">
            {/* Professional Logo */}
            <a href="/" className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-md">
                <BookOpen className="h-5 w-5" />
              </div>
              <span className="text-2xl font-bold text-gray-900">
                Classtopper
              </span>
            </a>

            {/* Desktop Navigation with Professional Dropdowns */}
            <div className="hidden items-center gap-8 lg:flex">
              {/* Courses Dropdown */}
              <div className="relative group">
                <button className="flex items-center gap-1 text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200 py-2">
                  Courses
                  <ChevronDown className="h-4 w-4 group-hover:rotate-180 transition-transform duration-200" />
                </button>
                
                <div className="absolute top-full left-0 mt-2 w-80 bg-white border border-gray-200 rounded-xl shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 z-50">
                  <div className="p-6 space-y-4">
                    {/* JEE Course */}
                    <a href="/courses/jee" className="flex items-start gap-3 p-3 rounded-lg hover:bg-blue-50 transition-colors group/item">
                      <div className="h-12 w-12 rounded-lg bg-blue-100 flex items-center justify-center text-blue-600 font-bold text-sm shadow-sm">
                        JEE
                      </div>
                      <div className="flex-1">
                        <div className="font-semibold text-gray-900 group-hover/item:text-blue-600 mb-1">JEE Main & Advanced</div>
                        <div className="text-xs text-gray-500">Complete Engineering Entrance Preparation</div>
                        <div className="text-xs text-blue-600 font-medium mt-1">Starting at ₹999/month</div>
                      </div>
                    </a>
                    
                    {/* NEET Course */}
                    <a href="/courses/neet" className="flex items-start gap-3 p-3 rounded-lg hover:bg-green-50 transition-colors group/item">
                      <div className="h-12 w-12 rounded-lg bg-green-100 flex items-center justify-center text-green-600 font-bold text-sm shadow-sm">
                        NEET
                      </div>
                      <div className="flex-1">
                        <div className="font-semibold text-gray-900 group-hover/item:text-green-600 mb-1">NEET UG</div>
                        <div className="text-xs text-gray-500">Medical Entrance Examination</div>
                        <div className="text-xs text-green-600 font-medium mt-1">Starting at ₹1,199/month</div>
                      </div>
                    </a>
                    
                    {/* Boards Course */}
                    <a href="/courses/boards" className="flex items-start gap-3 p-3 rounded-lg hover:bg-purple-50 transition-colors group/item">
                      <div className="h-12 w-12 rounded-lg bg-purple-100 flex items-center justify-center text-purple-600 font-bold text-sm shadow-sm">
                        12th
                      </div>
                      <div className="flex-1">
                        <div className="font-semibold text-gray-900 group-hover/item:text-purple-600 mb-1">Class 6-12 Boards</div>
                        <div className="text-xs text-gray-500">CBSE, ICSE & State Boards</div>
                        <div className="text-xs text-purple-600 font-medium mt-1">Starting at ₹799/month</div>
                      </div>
                    </a>
                    
                    {/* Foundation Course */}
                    <a href="/courses/foundation" className="flex items-start gap-3 p-3 rounded-lg hover:bg-orange-50 transition-colors group/item">
                      <div className="h-12 w-12 rounded-lg bg-orange-100 flex items-center justify-center text-orange-600 font-bold text-sm shadow-sm">
                        FN
                      </div>
                      <div className="flex-1">
                        <div className="font-semibold text-gray-900 group-hover/item:text-orange-600 mb-1">Foundation (6-10)</div>
                        <div className="text-xs text-gray-500">Strong Conceptual Foundation</div>
                        <div className="text-xs text-orange-600 font-medium mt-1">Starting at ₹599/month</div>
                      </div>
                    </a>
                  </div>
                </div>
              </div>

              {/* Regular Navigation Items */}
              <a href="/test-series" className="text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200">
                Test Series
              </a>
              <a href="/bookstore" className="text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200">
                CT Bookstore
              </a>
              <a href="/live-classes" className="text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200">
                Live Classes
              </a>
              <a href="/results" className="text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200">
                Results
              </a>

              {/* More Dropdown */}
              <div className="relative group">
                <button className="flex items-center gap-1 text-gray-700 hover:text-blue-600 font-medium transition-colors duration-200 py-2">
                  More
                  <ChevronDown className="h-4 w-4 group-hover:rotate-180 transition-transform duration-200" />
                </button>
                
                <div className="absolute top-full right-0 mt-2 w-56 bg-white border border-gray-200 rounded-xl shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 z-50">
                  <div className="p-4 space-y-2">
                    <a href="/free-content" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-green-500 text-lg">🎯</span>
                      <span className="text-sm font-medium text-gray-700 hover:text-blue-600">Free Content</span>
                    </a>
                    <a href="/doubt-solving" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-blue-500 text-lg">❓</span>
                      <span className="text-sm font-medium text-gray-700 hover:text-blue-600">Ask Doubts</span>
                    </a>
                    <a href="/scholarships" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-yellow-500 text-lg">🏆</span>
                      <span className="text-sm font-medium text-gray-700 hover:text-blue-600">Scholarships</span>
                    </a>
                    <hr className="my-2 border-gray-200" />
                    <a href="/careers" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-purple-500 text-lg">💼</span>
                      <span className="text-sm font-medium text-gray-700 hover:text-blue-600">Careers</span>
                    </a>
                    <a href="/about" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-gray-500 text-lg">ℹ️</span>
                      <span className="text-sm font-medium text-gray-700 hover:text-blue-600">About Us</span>
                    </a>
                    <a href="/contact" className="flex items-center gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors">
                      <span className="text-red-500 text-lg">📞</span>
                      <span className="text-sm font-medium text-gray-700 hover:text-blue-600">Contact</span>
                    </a>
                  </div>
                </div>
              </div>
            </div>

            {/* Professional CTA Buttons */}
            <div className="hidden items-center gap-4 lg:flex">
              <a
                href="tel:18001234567"
                className="flex items-center gap-2 text-sm font-medium text-gray-600 hover:text-blue-600 transition-colors"
              >
                <span className="text-green-500">📞</span>
                1800-123-4567
              </a>
              <a
                href="/login"
                className="inline-flex items-center justify-center px-5 py-2.5 text-sm font-medium text-gray-700 bg-white border-2 border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 transition-all duration-200"
              >
                Sign In
              </a>
              <a
                href="/register"
                className="inline-flex items-center justify-center px-6 py-2.5 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-all duration-200 shadow-sm hover:shadow-md"
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

          {/* Professional Mobile Menu */}
          {mobileMenuOpen && (
            <div className="lg:hidden border-t border-gray-200 bg-white">
              <div className="px-4 py-6 space-y-6">
                {/* Course Categories */}
                <div>
                  <div className="font-semibold text-gray-900 text-sm uppercase tracking-wide mb-3">COURSES</div>
                  <div className="space-y-2">
                    <a href="/courses/jee" className="flex items-center gap-3 p-3 rounded-lg hover:bg-blue-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-blue-100 flex items-center justify-center text-blue-600 text-xs font-bold">JEE</div>
                      <div>
                        <span className="text-sm font-medium text-gray-900">JEE Main & Advanced</span>
                        <div className="text-xs text-gray-500">₹999/month</div>
                      </div>
                    </a>
                    <a href="/courses/neet" className="flex items-center gap-3 p-3 rounded-lg hover:bg-green-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-green-100 flex items-center justify-center text-green-600 text-xs font-bold">NEET</div>
                      <div>
                        <span className="text-sm font-medium text-gray-900">NEET UG</span>
                        <div className="text-xs text-gray-500">₹1,199/month</div>
                      </div>
                    </a>
                    <a href="/courses/boards" className="flex items-center gap-3 p-3 rounded-lg hover:bg-purple-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-purple-100 flex items-center justify-center text-purple-600 text-xs font-bold">12th</div>
                      <div>
                        <span className="text-sm font-medium text-gray-900">Class 6-12 Boards</span>
                        <div className="text-xs text-gray-500">₹799/month</div>
                      </div>
                    </a>
                    <a href="/courses/foundation" className="flex items-center gap-3 p-3 rounded-lg hover:bg-orange-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      <div className="h-8 w-8 rounded bg-orange-100 flex items-center justify-center text-orange-600 text-xs font-bold">FN</div>
                      <div>
                        <span className="text-sm font-medium text-gray-900">Foundation</span>
                        <div className="text-xs text-gray-500">₹599/month</div>
                      </div>
                    </a>
                  </div>
                </div>

                {/* Services */}
                <div>
                  <div className="font-semibold text-gray-900 text-sm uppercase tracking-wide mb-3">SERVICES</div>
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

                {/* Support */}
                <div>
                  <div className="font-semibold text-gray-900 text-sm uppercase tracking-wide mb-3">SUPPORT</div>
                  <div className="space-y-2">
                    <a href="/scholarships" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Scholarships
                    </a>
                    <a href="/about" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      About Us
                    </a>
                    <a href="/contact" className="block p-3 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-50 transition-colors" onClick={() => setMobileMenuOpen(false)}>
                      Contact
                    </a>
                  </div>
                </div>

                {/* Auth Buttons */}
                <div className="space-y-3 pt-4 border-t border-gray-200">
                  <a
                    href="/login"
                    className="block w-full text-center px-4 py-3 text-sm font-medium text-gray-700 bg-white border-2 border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 transition-colors"
                  >
                    Sign In
                  </a>
                  <a
                    href="/register"
                    className="block w-full text-center px-4 py-3 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors"
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

      {/* Page Sections with Professional Styling */}
      <main className="bg-white">
        {/* Hero Section */}
        <HeroSection />

        {/* Special Offers Section */}
        <div className="bg-gray-50">
          <SpecialOffers />
        </div>

        {/* Teacher Highlight Section */}
        <div className="bg-white">
          <TeacherHighlight />
        </div>

        {/* Exam Categories Section */}
        <div className="bg-gray-50">
          <CategoryGrid />
        </div>

        {/* Academic Excellence Section */}
        <div className="bg-white">
          <AcademicExcellence />
        </div>
      </main>

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default HomePage;
