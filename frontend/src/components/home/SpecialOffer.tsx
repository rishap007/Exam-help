import { Clock, Users, Award, CheckCircle } from 'lucide-react';

export const SpecialOffers = () => {
  return (
    <section className="py-16 bg-gradient-to-br from-orange-50 to-red-50">
      <div className="container mx-auto px-4">
        {/* Diwali Special Header */}
        <div className="text-center mb-12">
          <div className="inline-flex items-center gap-2 bg-orange-100 px-4 py-2 rounded-full text-orange-600 font-medium mb-4">
            🪔 #DiwaliWithClasstopper
          </div>
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Festival of Learning Special Offers
          </h2>
          <p className="text-muted-foreground max-w-2xl mx-auto">
            Light up your academic journey with our biggest discounts of the year. Limited time offers!
          </p>
        </div>

        {/* Offer Cards Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {/* JEE Offer Card */}
          <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow border-t-4 border-blue-500">
            <div className="flex items-center justify-between mb-4">
              <span className="bg-blue-100 text-blue-600 px-3 py-1 rounded-full text-sm font-medium">
                JEE 2026
              </span>
              <span className="bg-red-100 text-red-600 px-2 py-1 rounded text-xs font-bold">
                70% OFF
              </span>
            </div>
            <h3 className="text-xl font-bold mb-2">Foundation Course</h3>
            <div className="flex items-center gap-2 mb-4">
              <span className="text-2xl font-bold text-blue-600">₹999</span>
              <span className="text-sm text-gray-500 line-through">₹3,299</span>
            </div>
            <ul className="space-y-2 mb-6 text-sm text-gray-600">
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Live Classes by Top Faculty
              </li>
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Complete Study Material
              </li>
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Free Test Series
              </li>
            </ul>
            <a
              href="/courses/jee-foundation"
              className="block w-full text-center bg-blue-600 text-white py-3 rounded-lg font-medium hover:bg-blue-700 transition-colors"
            >
              Enroll Now
            </a>
          </div>

          {/* NEET Offer Card */}
          <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow border-t-4 border-green-500">
            <div className="flex items-center justify-between mb-4">
              <span className="bg-green-100 text-green-600 px-3 py-1 rounded-full text-sm font-medium">
                NEET 2025
              </span>
              <span className="bg-red-100 text-red-600 px-2 py-1 rounded text-xs font-bold">
                60% OFF
              </span>
            </div>
            <h3 className="text-xl font-bold mb-2">Crash Course</h3>
            <div className="flex items-center gap-2 mb-4">
              <span className="text-2xl font-bold text-green-600">₹1,999</span>
              <span className="text-sm text-gray-500 line-through">₹4,999</span>
            </div>
            <ul className="space-y-2 mb-6 text-sm text-gray-600">
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Intensive Revision Classes
              </li>
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Previous Years Papers
              </li>
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Mock Tests
              </li>
            </ul>
            <a
              href="/courses/neet-crash"
              className="block w-full text-center bg-green-600 text-white py-3 rounded-lg font-medium hover:bg-green-700 transition-colors"
            >
              Enroll Now
            </a>
          </div>

          {/* Free Trial Card */}
          <div className="bg-white rounded-xl p-6 shadow-lg hover:shadow-xl transition-shadow border-t-4 border-purple-500">
            <div className="flex items-center justify-between mb-4">
              <span className="bg-purple-100 text-purple-600 px-3 py-1 rounded-full text-sm font-medium">
                All Courses
              </span>
              <span className="bg-green-100 text-green-600 px-2 py-1 rounded text-xs font-bold">
                FREE
              </span>
            </div>
            <h3 className="text-xl font-bold mb-2">7-Day Free Trial</h3>
            <div className="flex items-center gap-2 mb-4">
              <span className="text-2xl font-bold text-purple-600">₹0</span>
              <span className="text-sm text-gray-500">No Credit Card Required</span>
            </div>
            <ul className="space-y-2 mb-6 text-sm text-gray-600">
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Access to Live Classes
              </li>
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Free Doubt Solving
              </li>
              <li className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4 text-green-500" />
                Study Material Preview
              </li>
            </ul>
            <a
              href="/free-trial"
              className="block w-full text-center bg-purple-600 text-white py-3 rounded-lg font-medium hover:bg-purple-700 transition-colors"
            >
              Start Free Trial
            </a>
          </div>
        </div>

        {/* Urgency Timer */}
        <div className="text-center mt-12">
          <div className="inline-flex items-center gap-4 bg-red-100 px-6 py-3 rounded-lg text-red-600">
            <Clock className="h-5 w-5" />
            <span className="font-medium">Offer ends in: <strong>4 days 12 hours</strong></span>
          </div>
        </div>
      </div>
    </section>
  );
};
